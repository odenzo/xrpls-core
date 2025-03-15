package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.{ XrplCurrency, XrplStdCurrency }
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/** https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/account-methods/account_currencies#account_currencies */
object AccountCurrencies extends XrpCommand[AccountCurrencies.Rq, AccountCurrencies.Rs] {

  case class Rq(
      account: AccountAddress,
      ledgerIndex: LedgerHandle = LedgerHandle.validated,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.ACCOUNT_CURRENCIES
  }
  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * Currency can be ISO style (USD, NZD or full hex string. XrplCurrency or
    * XrplStdCurrency In this case we put them all in the dyunamic XrplCurrency
    */
  case class Rs(
      ledgerIndex: Option[LedgerIndex],
      ledgerCurrentIndex: Option[LedgerIndex],
      ledgerHash: Option[LedgerHash],
      receiveCurrencies: List[XrplCurrency],
      sendCurrencies: List[XrplCurrency],
      validated: Option[Boolean],
  ) extends XrpCommandRs derives ConfiguredCodec
  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
