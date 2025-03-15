package com.odenzo.xrpl.communication.models

import cats.data.NonEmptyList
import com.odenzo.xrpl.models.api.commands.transaction.Submit
import io.circe.Json
import io.circe.derivation.Configuration

/**
  * The Json contains the initial request embedded, its actually the full
  * response if we got one. If any kind of exception was thrown its caught and
  * put in the exception and rethrown in an IO typically.
  */
case class XrplEngineTxnError(
    warnings: Option[NonEmptyList[XrplWarning]],
    errors: Option[NonEmptyList[XrplError]],
    exception: Option[Throwable],
    rs: Option[Json],
) extends Throwable("Exception Exucuting XrplCommand")

// TODO: Make some building so we can include a nested exception?

/** This is returned on a successful Command execution. */
case class XrplEngineTxnResult(
    warnings: Option[NonEmptyList[XrplWarning]],
    ledgerInfo: Option[ResponseLedgerInfo],
    submitted: Submit.Rs,
)

object XrplEngineTxnResult:
  given Configuration = Configuration.default.withSnakeCaseMemberNames

/* case class XrplEngineResult(engineResult: String, engineResultCode: Int, engineResultMessage: String)
 *
 * object XrplEngineResult:
 * given Configuration = Configuration.default.withSnakeCaseMemberNames */
