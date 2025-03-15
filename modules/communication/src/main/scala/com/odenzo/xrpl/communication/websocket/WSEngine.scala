package com.odenzo.xrpl.communication.websocket

import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication
import com.odenzo.xrpl.communication.XrplEngine
import com.odenzo.xrpl.communication.models.*
import com.odenzo.xrpl.communication.models.ResponseExtractors.{ extractStatus, findResultRecord }
import com.odenzo.xrpl.communication.websocket.WSEngine.enrichOutboundMessageEnvelope

import com.odenzo.xrpl.models.api.commands.transaction.Submit
import com.odenzo.xrpl.models.api.commands.transaction.Submit as engine
import com.odenzo.xrpl.models.api.transactions.support.{ TxCommon, XrpTxn }
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.api.commands.admin.{ LedgerAccept, Sign }
import com.odenzo.xrpl.models.internal.Wallet
import com.tersesystems.blindsight.LoggerFactory
import io.circe.pointer.literal.pointer
import io.circe.{ ACursor, Decoder, Encoder, Json, JsonObject }
import io.circe.syntax.given
import org.http4s.Uri
import scodec.bits.BitVector

import java.util.UUID

/**
  * WSService - Client facing APIs to send and receive WebSocket based messages.
  * For completeness I guess, normally RPC is good enough. There is a seperate
  * SubcribeEngine. Both this and Subscribe engine will be simplified rather
  * than combined.
  */
class WSEngine(transport: WebSocketTransport) extends XrplEngine {

  private val log = LoggerFactory.getLogger

  /**
    * This sends non-transactional WebSocket commands, it is 'monadic' in the it
    * sends and will block a virtual thread (fibre) until it receives the
    * results. This doesn't block other fibres sending requets and receiving
    * responses as well.
    */
  override def send[RQ <: XrpCommandRq: Encoder.AsObject: Decoder, RS <: XrpCommandRs: Encoder.AsObject: Decoder](
      message: RQ
  ): IO[XrplEngineCommandResult[RS]] = {
    val outbound: JsonObject = enrichOutboundMessageEnvelope(message, message.asJsonObject)
    log.info(s"Outbound WebSocket Message: ${outbound.toJson.spaces4}")
    for {
      payload <- transport.send(outbound).map(_.toJson)
      status  <- ResponseExtractors.extractStatus(payload)
      result  <- status match
                   case XrplStatus.success =>
                     ResponseExtractors.findResultRecord(payload).flatMap(ResponseExtractors.extractSuccessResult[RS])
                   case _                  => IO.raiseError(NotImplementedError("Error Cases Not Handled for WSEngine Commands"))
      // extractAndThrowErrors[RS](payload)
    } yield result
  }

  /**
    * This sends transactional WebSocket. Need to think about ticketing and
    * sequence and TxCommon autofilling
    */
  override def sendTxn[T <: XrpTxn: Encoder.AsObject: Decoder](
      commonTx: TxCommon,
      txn: T,
      wallet: Wallet,
  ): IO[XrplEngineTxnResult] = {
    for {
      _         <- IO(log.info(s"Sending Transaction ${txn.asJson.spaces4} to WebSocket"))
      txJson    <- WSEngine.formatTxJsonForTxnSigning[T](txn, commonTx)
      signed    <- signTxn(txJson = txJson, wallet)
      submitted <- submitSignedTxn(signed.rs)
      _          = log.info("Txn Applied ")
    } yield submitted
  }

  override def ledgerAccept: IO[XrplEngineCommandResult[LedgerAccept.Rs]] =
    send[LedgerAccept.Rq, LedgerAccept.Rs](LedgerAccept.Rq())

  // TODO: Pull out Sign.Rq to a function (wallet, txJson) and use with both engines.
  private[websocket] def signTxn(txJson: JsonObject, wallet: Wallet): IO[XrplEngineCommandResult[Sign.Rs]] = {
    log.debug(s"--- Signing With Key for ${wallet.publicKey}:\n ${txJson.asJson.spaces4} \n---")
    val rq: Sign.Rq = Sign.Rq(wallet.keyType, wallet.masterSeed.some, None, txJson)
    send[Sign.Rq, Sign.Rs](rq)
  }

  /**
    * Keeping this simple and putting helpers in the sendTxn level.
    *
    * @throws XrplEngineTxnError
    *   If Submit understood the message by got an invalid engine tec_code
    * @throws XrplEngineCommandError
    *   If there was a problem with the Signing or Submit of the Txn
    */
  private[websocket] def submitSignedTxn[T <: XrpTxn: Decoder](
      signed: Sign.Rs
  ): IO[XrplEngineTxnResult] = {

    val rq: Submit.Rq = Submit.Rq(signed.txBlob)
    send[Submit.Rq, Submit.Rs](rq)
      .reject {
        case v if v.rs.engineResultCode != 0 =>
          val error = XrplError("XRP Engine Error", v.rs.engineResultCode.some, v.rs.engineResultMessage.some)
          XrplEngineTxnError(v.warnings, NonEmptyList.one(error).some, None, v.rs.asJson.some)
      }.map { submitted =>
        XrplEngineTxnResult(submitted.warnings, submitted.ledgerInfo, submitted.rs)
      }
  }

}

object WSEngine {

  private val log = LoggerFactory.getLogger

  def createAsResource(wsAdmin: Uri): Resource[IO, WSEngine] = {
    val transport: Resource[IO, WebSocketTransport] = WebSocketTransport.build(wsAdmin)
    transport.map(r => WSEngine(r))
  }

  /** Adds a unique ID and formats the command. */
  def enrichOutboundMessageEnvelope[T <: XrpCommandRq](message: T, jo: JsonObject): JsonObject = {
    val existing: List[(String, Json)] = jo.toList
    val prepended                      = List(
      ("id", Json.fromString(UUID.randomUUID().toString)),
      ("command", message.command.label.asJson),
    )
    val appended                       = List(("api_version", Json.fromInt(2)))
    val fullList: List[(String, Json)] = prepended ++ existing ++ appended
    JsonObject(fullList*)
  }

  /**
    * This takes a CommandRq and formats in WebSocket style. For now we are
    * automatically adding a UUID id, and the command. Same as
    * enrichOutboundMessageEnveelope but doesn't care about field order
    */
  private[websocket] def formatMessage[T <: XrpCommandRq: Encoder.AsObject](message: T): JsonObject = {
    val jo = message.asJsonObject
    jo.add("id", UUID.randomUUID().asJson)
    jo.add("command", message.command.toString.asJson)
    jo.add("api_version", Json.fromInt(2))
  }

  /**
    * Builds the Txn Request which is then sent to Sign (i.e. Building the
    * txjson to Sign.Rq
    */
  private[websocket] def formatTxJsonForTxnSigning[RQ <: XrpTxn: Encoder.AsObject](
      txnRq: RQ,
      txCommon: TxCommon,
  ): IO[JsonObject] = {
    for {
      _     <- IO(log.debug("Building Outbound Message"))
      merged = mergeTxnMetadata(txnRq, txCommon)
      // txjson = XrpBinCodecAPI.encode(merged.toJson, true)
    } yield merged
  }

  private[websocket] def mergeTxnMetadata[RQ <: XrpTxn: Encoder.AsObject](txnRq: RQ, txCommon: TxCommon): JsonObject = {
    val r = txnRq.asJsonObject
    val c = txCommon.asJsonObject
    r.deepMerge(c).add("TransactionType", txnRq.txnType.asJson)
  }

}
