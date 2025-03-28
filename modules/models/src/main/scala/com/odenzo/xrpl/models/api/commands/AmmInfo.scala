package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import com.odenzo.xrpl.models.data.models.monetary.{ BookCurrency, Script }
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Json, JsonObject }

/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/path-and-order-book-methods/amm_info
  *
  * Gets info about Automated Market Maker
  */
object AmmInfo extends XrpCommand[AmmInfo.Rq, AmmInfo.Rs] {
  import BookCurrency.given
  case class Rq(
      account: Option[AccountAddress]    = None,
      ammAccount: Option[AccountAddress] = None, // Script or script with XRP currency and no issuer
      asset: Option[Script], // Currency and Issuer
      asset2: Option[BookCurrency], // Currency and Issuer
      ledgerHash: Option[LedgerHash]     = None,
      ledgerIndex: LedgerHandle          = validated,
      limit: Option[Long]                = None,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.AMM_INFO
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(
      amm: Json,
      lpToken: Json,
      tradingFee: Drops,
      auctions_slot: JsonObject,
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
