package com.odenzo.xrpl.models.api.commands.accountinfo

import cats.Monoid
import com.odenzo.xrpl.models.api.commands.Command.{ACCOUNT_INFO, ACCOUNT_LINES}
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{XrpCommand, XrpCommandRq, XrpCommandRs, XrpScrolling}
import com.odenzo.xrpl.models.api.commands.serverinfo.Fee.{Rq, Rs}
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.TrustLine
import io.circe.Json
import io.circe.derivation.{Configuration, ConfiguredCodec}
import spire.algebra.Semigroup
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.data.models.atoms.{AccountAddress, LedgerHash}
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle

/**
  * Used to get the trust lines and their balances. For currencies other than
  * XRP. For XRP balance see AccountInfo
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/account-methods/account_lines#account_lines
 *
 * @param account
  * @param peer
  *   Limit lines to those between account the this account address
  * @param limit
  *   Ask (but not demand) rippled limit the number of results to this.
  * @param marker
  *   Scrolling marker (opaque)
  * @param ledger
  *   Get account lines as of this point in history, defaults to last validated
  *   ledger
  * @param id
  */
object AccountLines extends XrpCommand[AccountLines.Rq, AccountLines.Rs] with XrpScrolling {

  val InOut: (Rq.type, Rs.type) = (Rq, Rs)

  /** This is a scrollable command */
  case class Rq(
      account: AccountAddress,
      ledgerIndex: Option[LedgerHandle],
      ledgerHash: Option[LedgerHash],
      peer: Option[AccountAddress] = None,
  ) extends XrpCommandRq with XrpScrolling derives ConfiguredCodec {
    val command: Command = ACCOUNT_LINES
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults

  /// **
  //  * The result field portion for account_lines command response. Add the
  //  * optional ledger_index , ledger_current_index, ledger_hash
  //  */
  case class Rs(
      account: AccountAddress,
      lines: List[TrustLine],
      ledgerIndex: Option[LedgerIndex],
      ledgerCurrentIndex: Option[LedgerIndex],
      ledgerHash: Option[LedgerHash],
      validated: Option[Boolean] = false.some, // Using default values in decoder?
  ) extends XrpCommandRs with XrpScrolling derives ConfiguredCodec

  /**
    * Since its scrolling we should mark it and have a monoid combine defined,
    * but on RS no easy empty. Should and have a Semigroup for
    * lines:List[TrustLine] already, so in most cases uses a
    * `NonEmptyList[Rs](firstRs, others).map(_.lines).combineAll` after
    * importing standard Cats, see source code.
    */
  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults
    given Semigroup[Rs] = Semigroup.instance((x, y) => x.copy(lines = x.lines ++ y.lines))
  }
}
