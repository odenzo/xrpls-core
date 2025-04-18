package com.odenzo.xrpl.models.data.models.monetary

import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/** Atom that is found in account_lines result */
case class TrustLine(
    account: AccountAddress,
    balance: FiatValue,
    currency: XrplCurrency,
    limit: BigDecimal,
    limitPeer: BigDecimal,
    noRipple: Option[Boolean]     = Some(false),
    noRipplePeer: Option[Boolean] = Some(false),
    freeze: Option[Boolean]       = Some(false),
    freeze_peer: Option[Boolean]  = Some(false),
    qualityIn: Quality, // Have a Quality Model somewhere, TransferRAte?
    qualityOut: Quality,
) derives ConfiguredCodec {

  def toFiatAmount: CurrencyAmount.FiatAmount =
    CurrencyAmount.FiatAmount(balance, currency, account) //
}

object TrustLine {
  given Configuration = Configuration.default.withSnakeCaseMemberNames
}
