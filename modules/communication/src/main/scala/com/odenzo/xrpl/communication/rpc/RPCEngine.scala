package com.odenzo.xrpl.communication.rpc

import _root_.scodec.bits.*
import cats.*
import cats.effect.*
import cats.implicits.catsSyntaxOptionId
import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.communication.rpc.RPCEngine.{ createTxJsonBinary, given_Configuration, merge }
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.communication.models.{
  ResponseExtractors,
  RpcRequest,
  XrplEngineCommandResult,
  XrplEngineTxnResult,
}
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.transactions.support.{ TxCommon, XrpTxn }
import com.odenzo.xrpl.models.internal.Wallet
import com.odenzo.xrpl.models.scodecs.XrpBinCodecAPI
import com.tersesystems.blindsight.LoggerFactory
import io.circe.derivation.Configuration
import io.circe.optics.JsonPath.root
import io.circe.pointer.literal.pointer
import io.circe.syntax.given
import io.circe.{ Decoder, Encoder, Json, JsonObject }
import org.http4s.*
import org.http4s.circe.*
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.client.*
import org.http4s.client.middleware.RetryPolicy
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.*
import org.typelevel.ci.CIStringSyntax

import scala.concurrent.duration.*
import scala.util.Try

class RPCEngine(server: Uri, client: Client[IO]) extends XrplEngine with BlindsightLogging {
  private val log = LoggerFactory.getLogger

  log.info(s"Constructing RPCEngine pointing to port URI: $server")
  private val rpcRequest: Request[IO] =
    Request[IO](Method.POST).withContentType(`Content-Type`(MediaType.application.json)).withUri(server)

  /**
    * Calls the RPC endpoint. Either getting Network/HTTP error, or a bad JSON,
    * so the return type is a problem. Also, the command itself could have
    * failed with status != success and have some warnings.
    */
  def send[RQ <: XrpCommandRq: Encoder.AsObject: Decoder, RS <: XrpCommandRs: Encoder.AsObject: Decoder](
      rq: RQ
  ): IO[XrplEngineCommandResult[RS]] = {
    val command           = rq.command.label
    log.info(s"Executing Command [$command]")
    val json              = rq.asJson.deepDropNullValues
    val rpcRq: RpcRequest = RpcRequest(rq.command.label, List(json))
    wireLog.info(s"$command::RQ:\n ${rpcRq.asJson.spaces4}")
    client.run(rpcRequest.withEntity(rpcRq)).use { (response: Response[IO]) =>
      println("Hello")
      for {
        _      <- IO(log.info("Got the response"))
        rs     <- response.as[Json] // HTTP4S as to get the response as JSON
        _       = wireLog.info(s"$command::RS\n ${rs.spaces4}")
        result <- ResponseExtractors.extractCommandResult[RS](rs)
      } yield result
    }
  }

  /**
    * Given a XrpTxn with specific fields, and any TxCommon fields, this creates
    * the tx_json like Json Binary (only signing fields and sorted) and asks the
    * online XRPL to sign it. It then submits the signed txn to the server. It
    * should be the only exposed Txn method really, others are public for
    * testing only
    */
  override def sendTxn[T <: XrpTxn: Encoder.AsObject: Decoder](
      commonTx: TxCommon,
      txn: T,
      wallet: Wallet,
  ): IO[XrplEngineTxnResult] = {
    for {
      _         <- IO(log.info(s"Sending Transaction ${txn.asJson.spaces4} RPCEngine"))
      // TODO: Have something to fill out the autofill fields (lastLedger / fee etc iff empty).
      fullTxn    = merge(txn, commonTx)
      signed    <- signTxn(txJson = fullTxn, wallet)
      submitted <- submitSignedTxn(signed.rs) // We have the SubmitRs, lets seperate out its error handling as needed
      _         <- IO(log.info(s"Raw Submit Response ${submitted.asJson.spaces4}"))
      // TODO: We have to look for engine_error stuff in the raw response too, and decide if we want to
      // raise an error.
      // It could be that success=true will have engine errors? If success=false we get raised exception here
    } yield XrplEngineTxnResult(submitted.warnings, submitted.ledgerInfo, submitted.rs)
  }

  /**
    * When running in stand-along mode we need to manually advance the ledger by
    * sending a LedgerAccept. Can be done after 1 or N transactions
    */
  def ledgerAccept: IO[XrplEngineCommandResult[LedgerAccept.Rs]] = {
    log.debug("Accepting Ledger")
    val accept = LedgerAccept.Rq()
    send[LedgerAccept.Rq, LedgerAccept.Rs](accept)
  }

  /**
    * This uses the server to sign a JsonObject representing the Tx given secret
    * key information We now have no type Txn because we did a merge of the
    * JsonObjects. Really need to push that farther down the stack
    */
  private[rpc] def signTxn(txJson: JsonObject, wallet: Wallet): IO[XrplEngineCommandResult[Sign.Rs]] = {
    val rq: Sign.Rq = Sign.Rq(wallet.keyType, wallet.masterSeed.some, None, txJson)
    println(s"Full Sign.Rq: ${rq.asJson.spaces4}")
    val prog        = for {
      cmdResult <- send[Sign.Rq, Sign.Rs](rq)
      _          = println(s"Full Sign.Rs: ${cmdResult.rs.asJson.spaces4}")

    } yield cmdResult
    prog
  }

  /** Keeping this simple and putting helpers in the sendTxn level. */
  private[rpc] def submitSignedTxn(signed: Sign.Rs): IO[XrplEngineCommandResult[Submit.Rs]] = {
    log.info("In SubmitSignedTxn..")
    val rq: Submit.Rq                                = Submit.Rq(signed.txBlob, true)
    val prog: IO[XrplEngineCommandResult[Submit.Rs]] = for {
      response <- send[Submit.Rq, Submit.Rs](rq)
    } yield response
    prog
  }

}

object RPCEngine {
  private val log = LoggerFactory.getLogger

  private[rpc] val stdHeaders: Headers = Headers(Header.Raw(ci"Content-Type", "application/json"))

  /**
    * Merge the supplemental TxCommon into the base request (deep merge). TODO:
    * Probably should drop nulls here too.
    */
  private[rpc] def merge[RQ <: XrpTxn: Encoder.AsObject](txnRq: RQ, txCommon: TxCommon): JsonObject = {
    val r = txnRq.asJsonObject
    val c = txCommon.asJsonObject
    r.deepMerge(c).add("TransactionType", txnRq.txnType.asJson)
  }

  // Do we want to inject the TxJson into the object and return it?
  private[rpc] def createTxJsonBinary(jsonObject: JsonObject): BitVector =
    XrpBinCodecAPI.encode(jsonObject.toJson, true)

  /**
    * This is a pending part to catch transport level errors (or any other
    * exception above XRPL level)
    */
  private[rpc] def expectErrorHandler(requestInfo: Json)(rs: Response[IO]): IO[Throwable] = {
    val error = Throwable(s"Unexpected Error going JSON-RPC: ${rs.headers} ${rs.status}")
    log.error(s"Got An Error Response: ${rs} on call\n ${requestInfo.spaces4}", error)
    IO.pure(error)
  }

  given Configuration = Configuration.default.withSnakeCaseMemberNames

  def createAsResource(uri: Uri): Resource[IO, RPCEngine] = {
    EmberClientBuilder.default[IO].withIdleConnectionTime(1.minutes).build.map { c =>
      val engine: RPCEngine = RPCEngine(uri, c)
      engine
    }
  }

}
