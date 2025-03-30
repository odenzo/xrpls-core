package com.odenzo.xrpl.models.scodecs

import cats.*
import cats.data.*
import cats.implicits.*
import com.odenzo.xrpl.models.data.monetary
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.{ Drops, FiatAmount }
import com.odenzo.xrpl.models.data.monetary.{ CurrencyAmount, FiatValue, Script, XrplStdCurrency }
import com.odenzo.xrpl.models.scodecs.AmountCurrencyScodecs.{ currencyCustom, currencyIsoCode }
import com.tersesystems.blindsight.LoggerFactory
import scodec.*
import scodec.Codec.*
import scodec.bits.*
import scodec.codecs.*

import scala.util.Try

/**
  * https://xrpl.org/currency-formats.html#issued-currency-math This is a Codec
  * that can handle a simple XRP value or a FiatAmount (JSON Object) with value,
  * issuer and currency (BigDecimal, AccountAddress, XrplCurrency) where the
  * currency field contains either an ISO code (USD, NZD) or a custom currency
  * (160 bits)
  * https://xrpl.org/docs/references/protocol/binary-format#amount-fields
  * CurrencyAmount is an enum with Drops and FiatAmount so we try a simple
  * discriminator by type We also know that the first bit is 0 for XRP and 1 for
  * FiatAmounts.
  */
object AmountScodecs {
  import AccountScodecs.accountAddressCodec
  private val log = LoggerFactory.getLogger

  val dropsDelegate: Codec[Drops] = AmountDropsScodecs.xrpDropsCodec.withContext("D XRP CurrencyAmount")
//    .widenOpt(
//      (a: Drops) => a: CurrencyAmount, // A => B, Drops => CurrencyAmount
//      { // B => A CurrencyAmount => Drops
//        case x @ FiatAmount(amount, currency, issuer) =>
//          log.info(s"XRP DROPS: Couldnt Wided $x to FiatAmount") // Delegate got used for FiatAmount erronesously
//          None
//        case x @ CurrencyAmount.Drops(amount)         =>
//          log.info(s"XRP DROPS:  => Some $x ")
//          Some(xCurrencyAmount)
//      },
//    )

  val fiatDelegate: Codec[FiatAmount] = AmountFiatScodecs.fiatAmountCodec.withContext("D FiatAmount")
//    .widenOpt(
//      a => a: CurrencyAmount,
//      {
//        case x @ CurrencyAmount.FiatAmount(amount, currency, issuer) => Some(x)
//        case CurrencyAmount.Drops(amount)                            => None
//      },
//    )

  /**
    * This either decodes a ful XRP for Full FiatAmount (including value,
    * currency and issuer.
    * [[com.odenzo.xrpl.models.data.monetary.CurrencyAmount]] is an enum
    * with [[com.odenzo.xrpl.models.data.monetary.CurrencyAmount.Drops]]
    * or
    * [[com.odenzo.xrpl.models.data.monetary.CurrencyAmount.FiatAmount]]
    */
//  val amountCodec: Codec[CurrencyAmount] = discriminated[CurrencyAmount]
//    .by(bool(1))
//    .subcaseO(false)((l: CurrencyAmount) => {
//      log.info(s"XRPDrops Amount $l")
//      debugCurrencyAmmount(l)
//      Some(l: CurrencyAmount)
//    })(dropsDelegate)
//    .subcaseO(true)((l: CurrencyAmount) => {
//      log.info(s"FiatAmont with $l ${pprint.apply(l)}")
//      debugCurrencyAmmount(l)
//      Some(l: CurrencyAmount)
//    })(fiatDelegate)
//    .withToString("DCurrencyAmount").withContext("DCurrencyAmount")

  val amountCodec: DiscriminatorCodec[CurrencyAmount, Boolean] =
    discriminated[CurrencyAmount].by(bool(1)).typecase(true, dropsDelegate).typecase(false, fiatDelegate)

  def debugCurrencyAmmount(a: CurrencyAmount) = {
    a match
      case monetary.CurrencyAmount.FiatAmount(amount: FiatValue, currency, issuer) =>
        log.info(s"Fiat starts with ${FiatValue.fiatValueOps.packBigDecimal(amount)}")
      case monetary.CurrencyAmount.Drops(amount: Long)                             =>
        log.info(s"Drop with $amount, First bit ${BitVector.fromLong(amount, 64)}")
  }
}
