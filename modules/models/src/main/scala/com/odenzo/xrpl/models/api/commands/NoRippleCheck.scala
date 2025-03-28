package com.odenzo.xrpl.models.api.commands

import cats.data.NonEmptyList
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec, ConfiguredEnumCodec }

/** https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/account-methods/noripple_check#noripple_check */
object NoRippleCheck extends XrpCommand[NoRippleCheck.Rq, NoRippleCheck.Rs] {

  enum Role derives ConfiguredEnumCodec {
    case user, gateway
  }

  object Role:
    given Configuration = Configuration.default

  /**
    * @param account
    *   Account Address to check
    * @param role
    *   user or gateway
    * @param transactions
    *   Generate transaction information to fix any problems (delta from
    *   recommendations) e.g., User accounts should have Default Ripple disabled
    *   and No Ripple on all new trustlines. Wheras issuers should have Default
    *   Ripple Enabled and No Ripple turned off on all trust lines.
    * @param ledgerIndex
    *   Supports both ledger_hash and ledger_index (I hope still)
    * @param limit
    *   Number of records to return
    */
  case class Rq(
      account: AccountAddress,
      role: Role                        = Role.user,
      transactions: Boolean             = true,
      limit: Long                       = 300,
      ledgerHash: Option[LedgerHash]    = None,
      ledgerIndex: Option[LedgerHandle] = Some(LedgerHandle.validated),
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.NORIPPLE_CHECK
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * Oh, ledger_current_index returned if ask for "current". If ask for
    * validated then get the standard ldeger_hash and ledger_index. Since I
    * can't think of any reason not to do this on a valid or old historical
    * ledger, we ignore the ledger_current_index. This is probably a pattern
    * everywhere in Ripple... "problem" in quotes, because current is not
    * stable? * @param ledger_index
    *   - TODO: Decode Transactions by mapping TransactionType to enums and then
    *     what? Decode from xxxTxn?
    * @param resultLedger
    *   The ledger the results apply to.
    * @param problems
    *   Possible empty list.
    * @param transactions
    */
  case class Rs(
      problems: List[String],
      transactions: Option[List[Json]],
  ) extends XrpCommandRs derives ConfiguredCodec {
    def problemsNEL: Option[NonEmptyList[String]]  = NonEmptyList.fromList(problems)
    def transactionNEL: Option[NonEmptyList[Json]] = transactions.flatMap(NonEmptyList.fromList)
  }

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
