package com.odenzo.xrpl.models.api.commands.accountinfo

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.*
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import AccountInfo.{ Rq, Rs }
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.data.ledgertree.AccountData
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex

object AccountInfo extends XrpCommand[AccountInfo.Rq, AccountInfo.Rs] {

  // private val config: Configuration = Configuration.default.withSnakeCaseConstructorNames

  /**
    * https://ripple.com/build/rippled-apis/#account-info
    *
    * @param account
    *   Set to strict so only public key or Account Address, we only allow addr.
    * @param queue
    *   true is only valid when the ledger is
    * @param signer_lists
    * @param strict
    * @param ledger
    *   Ledger Hash (20byte hex) or LedgerIndex including keywords. A hash is
    *   encoder in field ledger_hash everything else as ledger_index. Should
    *   probably make a ledgerIndex | ledgerHash approach
    * @param id
    */
  case class Rq(
      account: AccountAddress,
      queue: Boolean            = false, // Can only be true is LedgerHandle is current ledger (not closed or validated)
      signerLists: Boolean      = true,
      ledgerIndex: LedgerHandle = LedgerHandle.validated,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.ACCOUNT_INFO
  }

  object Rq:
    given Configuration  = Configuration.default.withSnakeCaseMemberNames
    val command: Command = Command.ACCOUNT_INFO

  case class Rs(
      accountData: AccountData,
      accountFlags: Map[String, Boolean], // TODO Proper enumed value I guess, a a Circe Key Encoder
      queueData: Option[Json], // Docs say this is outside. Signers inside.
      ledgerIndex: Option[LedgerIndex],
      ledgerCurrentIndex: Option[LedgerIndex],
      ledgerHash: Option[LedgerHash],
      validated: Option[Boolean], // Use default value as true/false?
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    // I should check to see how far down the tree this goes if not overridden
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
