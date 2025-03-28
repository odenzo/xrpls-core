package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs, XrpScrolling }
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.Json
import io.circe.derivation.{ Configuration, ConfiguredCodec, ConfiguredEnumCodec }

object AccountObjects extends XrpCommand[AccountObjects.Rq, AccountObjects.Rs] {

  /** Types of AccountObjects. with state being the Trust Lines. */
  enum AccountObjectType derives ConfiguredEnumCodec:
    case bridge, check, depositPreauth, escrow, nftOoffer, nftPage, offer, paymentCchannel, signerLlist, state, ticket

  object AccountObjectType:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * TODO: This is not used yet, so not fully implemented See
    * https://ripple.com/build/rippled-apis/#account-objects Note that this has
    * scrolling results This seems to have some gaps in the documentation.
    */
  case class Rq(
      account: AccountAddress,
      ledgerIndex: LedgerHandle             = LedgerHandle.validated,
      deletionBlockersOnly: Option[Boolean] = None,
      `type`: Option[AccountObjectType]     = None,
  ) extends XrpCommandRq with XrpScrolling derives ConfiguredCodec {
    val command: Command = Command.ACCOUNT_OBJECTS
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(
      account: AccountAddress,
      accountObjects: List[Json],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
