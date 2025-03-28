package com.odenzo.xrpl.models.scodecs

import com.odenzo.xrpl.models.data.models.monetary.FiatValue
import com.tersesystems.blindsight.LoggerFactory
import scodec.*
import scodec.Codec.*
import scodec.codecs.*

/**
  * A FiatAmount contains a BigDecimal "amount" plus a Currency and Issuer. This
  * DOES NOT INCLUDE the highest bit wich is consumer by discriminator. It does
  * include the second bit as 0 that denotes sign, always positive for XRP This
  * handles the BigDecimal amount which should definate be at least an opaque
  * type or wrapper case class.
  *
  * The binary encoding of a BigDecimal into the XRPL format is expensive and
  * painful. This attempts to do encode into XRPL binary amd decode from.
  * Debatable if the constants should be stuffed into FiatAmount to enable
  * validation of BigDecimal amount there. Yes.
  */
object AmountFiatValueScodecs {

  private val log                               = LoggerFactory.getLogger
  private[scodecs] val minVal: BigDecimal       = BigDecimal("-9999999999999999E80")
  private[scodecs] val maxVal: BigDecimal       = BigDecimal("9999999999999999E80")
  private[scodecs] val minAbsAmount: BigDecimal = BigDecimal("1000000000000000E-96")
  private[scodecs] val maxPrecision: Int        = 15

  // The range for the exponent when normalized (as signed Int, +97 gives range 1 to 177 unsigned)
  private[scodecs] val minExponent: Int    = -96
  private[scodecs] val maxExponent: Int    = 80
  private[scodecs] val minMantissa: BigInt = BigDecimal("1e15").toBigInt // For normalizing not input
  private[scodecs] val maxMantissa: BigInt = BigDecimal("10e16").toBigInt - 1 // For normalizing not input

  /**
    * Just gets the "token amount" that starts with 1, has a sign bit, 2comp
    * exponent, and significant digits. Assuming exponent is signed
    */
  val fiatValueCodec: Codec[FiatValue] =
    val codec: Codec[(Boolean, Int, Long)] = (bool(1) :: uint8 :: ulong(54))
    codec
      .xmap[FiatValue](
        (x: (Boolean, Int, Long)) => FiatValue.fiatValueOps.toFiatValue.tupled(x),
        (y: FiatValue) => FiatValue.fiatValueOps.packBigDecimal(y),
      )
      .withContext("Fiat Value Codec")
      .withToString("XRPL Fiat Value")

  /** Gets JUST the amount for a Fiat Value, not the currency or issuer */

}
