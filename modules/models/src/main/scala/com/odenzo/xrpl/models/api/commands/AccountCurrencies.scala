package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.{ XrplCurrency, XrplStdCurrency }
import io.circe.Codec
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/** https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/account-methods/account_currencies#account_currencies */
object AccountCurrencies extends XrpCommand[AccountCurrencies.Rq, AccountCurrencies.Rs] {

  case class Rq(
      account: AccountAddress,
      ledgerIndex: LedgerHandle = LedgerHandle.validated,
  ) extends XrpCommandRq {
    val command: Command = Command.ACCOUNT_CURRENCIES
  }
  object Rq:
    given Codec.AsObject[Rq] = ConfiguredCodec.derived(using CirceCodecUtils.configSnakes)
    // given Configuration =

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
  ) extends XrpCommandRs

  object Rs:
    given Codec.AsObject[Rs] = ConfiguredCodec.derived(using CirceCodecUtils.configSnakes)

}
