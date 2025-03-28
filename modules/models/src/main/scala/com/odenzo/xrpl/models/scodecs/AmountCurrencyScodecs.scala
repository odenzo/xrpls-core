package com.odenzo.xrpl.models.scodecs

import cats.*
import cats.data.*
import cats.implicits.*
import com.odenzo.xrpl.common.binary.FixedSizeBinary
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, Script, XrplCurrency, XrplStdCurrency }
import com.odenzo.xrpl.models.scodecs.AmountFiatScodecs.currency
import com.odenzo.xrpl.models.scodecs.AmountScodecs.{ dropsDelegate, fiatDelegate }
import com.tersesystems.blindsight.LoggerFactory
import scodec.*
import scodec.bits.*
import scodec.codecs.*

import java.nio.charset.StandardCharsets
import scala.util.Try

object AmountCurrencyScodecs {
  import AccountScodecs.accountAddressCodec
  import XrplCurrency.*

  private val log = LoggerFactory.getLogger

  /**
    * This Codec applies to 3 char currency codes, not the full XRPLCurrency
    * which can be 160 bits of data
    */
  private[scodecs] val currencyIsoCode: Codec[XrplCurrency] = {
    (constant(BitVector.low(7)) ~> constant(BitVector.low(88)) ~> fixedSizeBits(24, ascii) <~ constant(
      BitVector.low(40)
    )).withContext("ISO Currency")
      .xmap[XrplCurrency](
        (isoLike: String) => { // This gets validated because it might be ASCII but not XRP Valid Subset of ASCII
          // No Need to validate in real life, because XrplCurrency should always be valid (barring a bug!)
          log.info(s"Already extracted ASCII: [$isoLike]]")
          val currency: XrplCurrency = XrplCurrency.fromIsoLikeString(isoLike)
          currency
        },
        (y: XrplCurrency) => { // The const should be added back in, so just return the 24 bits that are ASCII as ASCII
          log.info(s"Encoding $y to XrplCurrency Binary")
          import XrplCurrency.given
          val code = XrplCurrency.toIsoString(y) // This string will be in default encoding (not ASCII)
          log.info(s"Currency Code: ${code}  ${code.getBytes(StandardCharsets.US_ASCII).length}")
          code
        },
      )
  }

  /**
    * 1 byte header is handled in the discriminator, which will actually set any
    * non-zero byte to 0xFF
    */
  private[scodecs] val currencyCustom: Codec[XrplCurrency] =
    codecs
      .bits(159)
      .withContext("currencyCustom")
      .xmap[XrplCurrency]( // 8 + 88 + 24 + 40 = 8 + 152
        (x: BitVector) => XrplCurrency.fsb.fromBits(BitVector.bit(false) ++ x),
        (y: XrplCurrency) => y.asBitVector.drop(1),
      )

  /**
    * XrplCurrency comes in two variants, 160bits or three ASCII letters
    * embedded in 160bits. The ISO Code is easier to ensure, because surrounding
    * things are all zero. We can discrimnate on the first 8 bits being zero
    * indicates ISO Code, otherwise Customer 160 bit CUrrency.
    *
    * I can use a discriminator to decide here I guess, since no one will
    * directory access the Code and Custom scodecs. This will consume the 8-bit
    * discrinator. Not sure what it will do on encoding yet. Seems like must be
    * FF to be true not anything but zero. This discriminate stuff sucks or more
    * likely I am missing something obvious.
    */
//  val xrplCurrencyCodec: Codec[XrplCurrency] = discriminated[XrplCurrency]
//    .by(bool(8))
//    .subcaseO(true)(l => Some(l))(currencyCustom)
//    .subcaseO(false)((l: XrplCurrency) => Some(l))(currencyIsoCode)

  /** We consume the first bit to determine if its ISO or customer format. */
  val bimodalCurrencyCodec: Codec[XrplCurrency] = {
    bool(1).consume(firstBitSet => {
      log.info(s"FirstBitSet: $firstBitSet")
      if firstBitSet then currencyIsoCode else currencyCustom
    })((b: XrplCurrency) => {
      log.info(s"Starting of XRPL Currency $b -> ${b.isIso}")
      b.isIso
    })

  }
  private val zipFn: Boolean => Codec[XrplCurrency] = (firstBitSet: Boolean) => {
    if firstBitSet
    then currencyIsoCode
    else currencyCustom
  }

  /**
    * I am not sure why there isn't a flatMap on a Codec or why this doesn't
    * work should just be A => Codec[B] to get Codec[(A,B)] Codec[A].flatMap(A
    * \=> Decoder[B]) but I want Codec[A].flatMap(A => Decoder[B])
    */
  val flatZipCodec: Codec[(Boolean, XrplCurrency)] = bool(1).flatZip(zipFn)

}
