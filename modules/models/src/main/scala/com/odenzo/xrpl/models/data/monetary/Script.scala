package com.odenzo.xrpl.models.data.monetary

import _root_.scodec.bits.BitVector
import com.odenzo.xrpl.common.binary.FixedSizeBinary
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.FiatAmount
import io.circe.*

/**
  * This is the type of currency for Token/Fiat money, it can be an ISO Amount
  * or a custom random bits. There is also BookCurrency for XRP | Currency for
  * dealing with BookOffers
  * @param currency
  *   Standard currency code, CANNOT BE XRP though.
  * @param issuer
  *   The account that issues the currency
  */
case class Script(currency: XrplCurrency, issuer: AccountAddress) derives Codec.AsObject {

  /**
    * Gets the packed version of currency and AccountAddress as 160-bit
    * accountId (no prefix/checksum)
    */
  def asBinary(using b: FixedSizeBinary[XrplCurrency]): BitVector = currency.asBits ++ issuer.asBits
}

object Script {

  extension (s: Script)
    def amount(fv: FiatValue): CurrencyAmount.FiatAmount = FiatAmount(fv, s.currency, s.issuer)
    def amount(sfv: String): CurrencyAmount.FiatAmount   = FiatAmount(FiatValue(sfv), s.currency, s.issuer)

}
