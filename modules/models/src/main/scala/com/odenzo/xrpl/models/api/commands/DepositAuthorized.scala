package com.odenzo.xrpl.models.api.commands

import cats.data.NonEmptyList
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import io.circe.Json
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/path-and-order-book-methods/deposit_authorized
  * Check if authorized to deposit. This should be done, but isn't yet. Its
  * still in Beta for Amendment
  */
object DepositAuthorized extends XrpCommand[DepositAuthorized.Rq, DepositAuthorized.Rs] {
  case class Rq(
      sourceAccount: AccountAddress,
      destinationAccount: AccountAddress,
      credentials: Option[NonEmptyList[Json]], // TODO: Credentials to take into account, need to investigate
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.DEPOSIT_AUTHORIZED
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  case class Rs(
      credentials: List[String],
      depositAuthorized: Boolean,
      sourceAccount: AccountAddress,
      destinationAccount: AccountAddress,
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
