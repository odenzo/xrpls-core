package com.odenzo.xrpl.models.api.commands

import cats.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.api.commands.AccountOffers.{ Rq, Rs }
import com.odenzo.xrpl.models.api.commands.Command.ACCOUNT_OFFERS
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs, XrpScrolling }
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import io.circe
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object AccountOffers extends XrpCommand[Rq, Rs] {
  val command: Command = Command.ACCOUNT_OFFERS

  /** Get a list of outstanding offers created by an account. */
  case class Rq(
      account: AccountAddress,
      ledgerIndex: Option[LedgerHandle] = LedgerHandle.validated.some,
      ledgerHash: Option[LedgerHash]    = None,
  ) extends XrpCommandRq with XrpScrolling derives ConfiguredCodec {
    val command: Command = ACCOUNT_OFFERS
  }

  /**
    * We always provide a ledger_index in the request so don't worry about it in
    * response (documented as optional and not there if provided)
    */
  case class Rs(
      account: AccountAddress,
      offers: List[JsonObject], // FIXME: Account Offer not completed
  ) extends XrpCommandRs with XrpScrolling derives ConfiguredCodec

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  }

  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }
}
