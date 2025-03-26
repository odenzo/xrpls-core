package com.odenzo.xrpl.communication.models

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.CommandMarkers.XrpCommandRs
import com.odenzo.xrpl.models.data.models.atoms.LedgerHash
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{ LedgerIndex, WILDCARD_LEDGER }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.optics.JsonPath.root
import io.circe.pointer.PointerFailure
import io.circe.pointer.literal.pointer
import io.circe.{ Codec, Decoder, Encoder, Json }

import java.util.UUID

/**
  * These extract common values from Commands and Txn (SubmitRs) for pairing
  * with the main XrpCommandRs and TxnResponse onjects.
  *   - Allows validated, errors, warning, and ledger_index,
  *     ledger_current_index and ledger_hash to not be in XrpCommandRs etc
  *
  * These can be used with WebSocket and RPC engines, the actual `result` body
  * must be passed in not the raw response. Unless explicitly noted. This is a
  * subfield for RPC and the top-level response for WS
  */
object ResponseExtractors {
  import com.odenzo.xrpl.models.data.models.atoms.hash256.*

  private val log = LoggerFactory.getLogger

  /** Gets the /result field as Json or raises and error */
  def findResultRecord(payload: Json): IO[Json] = IO.fromEither(pointer"/result".get(payload))

  /**
    * Get the status field, success or error. For RPC works on the `result`
    * sub-element of JSON (suppled as rs)
    */
  def extractStatus(rs: Json): IO[XrplStatus] = {
    IO.fromOption(root.result.status.as[XrplStatus].getOption(rs))(IllegalStateException(s"No Status Found"))
      .flatTap(st => IO(log.info(s"Got Status: $st")))
  }

  def extractLedgerHash(rs: Json): Option[LedgerHash] = root.ledger_hash.as[LedgerHash].getOption(rs)

  /**
    * Left if current ledger, else right. Ledger is current/closed/validated.
    * Revisit
    */
  def extractLedgerIndex(rs: Json): IO[Option[Either[LedgerHandle.LedgerIndex, LedgerHandle.LedgerIndex]]] = {
    val ledgerIndexPts          = pointer"/ledger_index"
    val ledgerCurrentIndexPts   = pointer"/ledger_current_index"
    val ledgerValidatedIndexPts = pointer"/validated_ledger_index"

    // Boolean is "isCurrent" flag
    val firstAvailableLedgerInfo: Option[(Json, Boolean)] = ledgerCurrentIndexPts
      .getOption(rs).map(j => (j, false))
      .orElse(ledgerCurrentIndexPts.getOption(rs).map(j => (j, true)))
      .orElse(ledgerValidatedIndexPts.getOption(rs).map(j => (j, false)))

    firstAvailableLedgerInfo.traverse { (j, isCurrent) =>
      IO.fromEither(
        j.as[LedgerIndex].leftMap(failure => IllegalArgumentException(s"Filed to Decode Response LedgerIndex", failure))
      ).map(li => if isCurrent then li.asLeft else li.asRight)

    }

  }

  /**
    * This is applicable to both transactions and commands. Basically says if
    * the info is from a validated ledger.
    */
  def extractValidated(rs: Json): Option[Boolean] =
    root.validated.as[Boolean].getOption(rs).orElse(false.some)

  def extractResult[RS: Decoder](rs: Json): IO[RS] = {
    for {
      result <- IO.fromOption(root.result.json.getOption(rs))(IllegalStateException(s"No Result Found in Response"))
      _       = log.info(s"Got Result now Decoding to RS Model: ${result.spaces4}")
      rs     <- IO.fromEither(result.as[RS])
      _       = log.info("Done: $rs")
    } yield rs
  }

  def extraceEngineResultCode(rs: Json)                                = {}
  def extractWarnings(rs: Json): IO[Option[NonEmptyList[XrplWarning]]] =
    IO(None)

  def extractErrors(rs: Json): IO[Option[NonEmptyList[XrplError]]] =
    IO(Option.empty[NonEmptyList[XrplError]])

  /**
    * For when status is not success, and XRPL Reports Command errors Raises
    * wrapper throws XrplEngineCommandError in IO context.
    */
  def extractAndThrowErrors[RS <: XrpCommandRs: Encoder: Decoder](rs: Json): IO[XrplEngineCommandResult[RS]] = {
    // TODO: Create XRPL customer throwables, one with the error stuff and the full response JSON
    // TODO: Extract the actual errors
    val foo: IO[Option[NonEmptyList[XrplError]]] = extractErrors(rs)
    for {
      errors   <- extractErrors(rs)
      warnings <- extractWarnings(rs)
      exception = XrplEngineCommandError(warnings, errors, None, rs.some)
      raised   <- IO.raiseError[XrplEngineCommandResult[RS]](exception)
    } yield raised
  }

  /**
    * If an exception occurs during processing then this packages them up as a
    * response. No corrective action is taken, not even trying to extract
    * errors/warnings from a result. Typically there will be no JSON involved,
    * although it depends where the error occured.
    */
  def rethrowErrors[RS <: XrpCommandRs: Encoder: Decoder](
      error: Throwable,
      context: Option[Json],
  ): IO[XrplEngineCommandResult[RS]] =
    val raised: IO[XrplEngineCommandResult[RS]] =
      IO.raiseError[XrplEngineCommandResult[RS]](XrplEngineCommandError(None, None, Some(error), context))
    raised

  /**
    * The command result had a success status so we go and try and extract the
    * RS from the payload
    */
  def extractSuccessResult[RS <: XrpCommandRs: Decoder: Encoder](payload: Json): IO[XrplEngineCommandResult[RS]] = {
    for {
      _           <- IO(log.info(s"extracting success result from ${payload.spaces4}"))
      rs          <- extractResult[RS](payload)
      _            = log.info(s"The result element from the payload: ${pprint.apply(rs)}")
      warnings    <- extractWarnings(payload) // Optional
      // Should make these one Option[ResponseLedgerInfo]
      ledgerHash   = extractLedgerHash(payload) // Optional - WalletPropose and some other commands muck the pattern
      ledgerIndex <- extractLedgerIndex(payload) // Optional - and either ledger_current_index OR ledger_index
      isValidated  = extractValidated(payload) // Optional - not there on many commands, but there for txns
      isCurrent    = ledgerIndex.exists(_.isRight).some
      index        = ledgerIndex.map(_.merge)
      ledgerInfo   = (ledgerHash, index, isCurrent, isValidated).mapN(ResponseLedgerInfo.apply)
    } yield XrplEngineCommandResult(rs = rs, warnings = warnings, ledgerInfo)
  }

  // Routine used for parsing a normal (not Signing or Submit) command sent and received
  def extractCommandResult[RS <: XrpCommandRs: Decoder: Encoder](rs: Json): IO[XrplEngineCommandResult[RS]] = {
    println(s"Extracting Command Result from ${rs.noSpaces}")
    for {
      status <- extractStatus(rs)
      result <- status match
                  case XrplStatus.success                    =>
                    log.debug("Status was success  = Going to Extract Result")
                    extractSuccessResult[RS](rs)
                  case XrplStatus.failure | XrplStatus.error => extractAndThrowErrors[RS](rs)
    } yield result
  }
}

/**
  * The standatd envelope for successful WebSocket responses at the command
  * level (not txn level) See also CommandRs
  */
case class WsResponseEnvelope(
    id: UUID,
    status: String,
    `type`: String,
    warning: Seq[XrplWarning],
) derives Codec.AsObject

case class ResponseLedgerInfo(
    hash: LedgerHash,
    index: LedgerIndex,
    isCurrent: Boolean,
    isValidated: Boolean,
) derives Codec.AsObject
