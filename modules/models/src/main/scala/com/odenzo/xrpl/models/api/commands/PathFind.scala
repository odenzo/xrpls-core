package com.odenzo.xrpl.models.api.commands

import cats.data.NonEmptyList
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import io.circe.Json
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * This is the elaborate WebSocket only version of Path find that basically
  * keeps a ticker tape of available paths and costs. Not implementing yet. Will
  * fail.
  */
object PathFind extends XrpCommand[PathFind.Rq, PathFind.Rs] {

  case class Rq(
      sourceAccount: AccountAddress,
      destinationAccount: AccountAddress,
      credentials: Option[NonEmptyList[Json]], // TODO: Credentials to take into account, need to investigate
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.PATH_FIND
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
