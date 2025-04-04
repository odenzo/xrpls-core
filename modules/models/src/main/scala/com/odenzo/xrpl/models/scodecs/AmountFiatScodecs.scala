package com.odenzo.xrpl.models.scodecs

import cats.*
import cats.data.*
import cats.implicits.*
import com.odenzo.xrpl.common.binary.FixedSizeBinary
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.FiatAmount
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, FiatValue, Script, XrplCurrency, XrplStdCurrency }
import com.tersesystems.blindsight.LoggerFactory
import scodec.*
import scodec.bits.*
import scodec.codecs.*

import scala.util.Try

/**
  * https://xrpl.org/currency-formats.html#issued-currency-math ULong max is
  * 18,446,744,073,709,551,615 First bit is taken to indicate its not XRP.
  *
  * Well, this is a mess, because we have a few kinds of amounts. XRP amount, a
  * TokenAmount (amount + currency + issuer) and the currency can be a ISO or
  * free form Custom Currency. Note: This is not just the "numeric" amount,
  * handle it all.
  *
  *   - Stuffing a bit of this functionality down into the models. -54-bit
  *     mantissa normalized to (10^15 ,10^16-1)
  * 8-bit exponent which is math exponent encoded +97 (uint)
  */
object AmountFiatScodecs {

  import AccountScodecs.accountAddressCodec
  import XrplCurrency.*

  private val log = LoggerFactory.getLogger

  given currency: Codec[XrplCurrency]  = AmountCurrencyScodecs.bimodalCurrencyCodec
  given fiatValue: Codec[FiatValue]    = AmountFiatValueScodecs.fiatValueCodec
  given address: Codec[AccountAddress] = AccountScodecs.accountAddressCodec

  val fiatAmountCodec: Codec[CurrencyAmount.FiatAmount] =
    (fiatValue :: currency :: address).as[FiatAmount].withContext("fiatAmount")

}
