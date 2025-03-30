package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.AccountChannels.{ Rq, Rs }
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.RippleChannel
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import io.circe.derivation.{ Configuration, ConfiguredCodec }

///**
//  * https://ripple.com/build/rippled-apis/#account-channels
//  *
//  * @param account
//  * @param destination_account
//  * @param ledger
//  * @param id
//  * @param marker
//  */
object AccountChannels extends XrpCommand[AccountChannels.Rq, AccountChannels.Rs] {
  val command: Command = Command.ACCOUNT_CHANNELS

  given stdConf: Configuration = Configuration.default.withSnakeCaseMemberNames

  /** Scrtollable */
  case class Rq(
      account: AccountAddress,
      destinationAccount: AccountAddress,
      ledgerIndex: LedgerHandle = LedgerHandle.validated,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = AccountChannels.command

  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(
      account: AccountAddress,
      channels: List[RippleChannel],
      ledgerIndex: Option[LedgerIndex],
      ledgerCurrentIndex: Option[LedgerIndex],
      ledgerHash: Option[LedgerHash],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
