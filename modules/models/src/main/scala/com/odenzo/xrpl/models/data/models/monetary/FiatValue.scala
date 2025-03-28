package com.odenzo.xrpl.models.data.models.monetary

import com.tersesystems.blindsight.LoggerFactory
import io.circe.{ Codec, Decoder, Encoder }
import scodec.bits.{ BitVector, bin }

import scala.annotation.unused

opaque type FiatValue = BigDecimal

object FiatValue {

  private val log                               = LoggerFactory.getLogger
  // To do: Validate max/min
  def apply(exact: String): FiatValue           = BigDecimal.exact(exact)
  def fromBigDecimal(bd: BigDecimal): FiatValue = bd

  extension (fv: FiatValue) def toBigDecimal: BigDecimal = fv

  given fiatValueCodec: Codec[FiatValue] = Codec.from(Decoder.decodeBigDecimal, Encoder.encodeBigDecimal)

  object fiatValueOps {

    val zero: BitVector          = BitVector.low(64).set(0)
    val minVal: BigDecimal       = BigDecimal("-9999999999999999E80")
    val maxVal: BigDecimal       = BigDecimal("9999999999999999E80")
    val minAbsAmount: BigDecimal = BigDecimal("1000000000000000E-96")
    val maxPrecision: Int        = 15

    // The range for the exponent when normalized (as signed Int, +97 gives range 1 to 177 unsigned)
    // THESE ARE NOT validation values for input.
    // ALthough fromBinary should be normalized already
    // TODO: Playing with literal number types
    @unused
    private val minExponent: -96    = -96
    private val maxExponent: Int    = 80
    private val minMantissa: BigInt = BigDecimal("1e15").toBigInt // For normalizing not input
    private val maxMantissa: BigInt = BigDecimal("10e16").toBigInt - 1 // For normalizing not input

    /**
      * This takes a valid BigDecimal amount and encodes it as a BitVector We
      * should be validated the enum constructions but not sure how to do
      * besides an (optional) builder way. This is a bit of a hack as it returns
      * to match the scodecs, and things must be converted to bits elsewhere.
      * Not this doesn't include the initial "Not XRP Bit" (1) which needs to be
      * added.
      * @return
      *   Is it Positive, the exponent and the mantissa
      */
    def packBigDecimal(bd: FiatValue): (Boolean, Int, Long) = {
      require(bd >= minVal, s"fiat value amount too small $minVal > $bd")
      require(bd <= maxVal, s"fiat value amount too big $maxVal < $bd")
      require(bd.abs >= minAbsAmount || bd == 0,
              s"fiat value amount too close to zero, Min Delta  $minAbsAmount < ${bd.abs}",
             )
      log.debug(s"Padding to BigDecimal (sig, exponent, mantissa) form: $bd")
      bd match {
        case a if a == 0 => (false, 0, 0)
        case amt         =>
          // Okay, now lets get a mantissa that fits in a ULong, or better in 10^15 .. 10^16-1 to fit in 54-bits
          val shiftPlaces: Int       = 16 + (amt.scale - amt.precision)
          val normalized: BigDecimal = amt * BigDecimal.exact(10).pow(shiftPlaces)
          val exp                    = -shiftPlaces + 97
          val isPositive             = bd.signum > -1
          normalized.isWhole match {
            case false => throw IllegalArgumentException(s"Unsure how to handle too much precision so error $bd")
            case true  =>
              log.debug(s" $amt -> $isPositive $exp ${normalized.longValue}")
              (isPositive, exp, normalized.longValue)
          }
      }
    }

    def packedToBinary(positive: Boolean, exponent: Int, mantissa: Long): BitVector = {
      val prefix: BitVector = if positive then bin"1" else bin"0"
      val exponentBits      = BitVector.fromInt(exponent, 8)
      val mantissaBits      = BitVector.fromLong(mantissa.longValue, 54)
      prefix ++ exponentBits ++ mantissaBits
    }

    def asBitsNoPrefix(v: FiatValue): BitVector = packedToBinary.tupled(packBigDecimal(v))

    /** Reconstructs BigDecimal from the sign, exponent and mantissa */
    def toFiatValue(isPositive: Boolean, exponent: Int, mantissa: Long): FiatValue = {

      log.debug(s"Unpacking to BigDecimal: Sign:  $isPositive Raw Exponent $exponent Raw Mantissa: $mantissa")
      val exponentAdj           = exponent - 97
      val answer: BigDecimal    = BigDecimal(mantissa) * BigDecimal.exact(10).pow(exponentAdj)
      val signedAns: BigDecimal = if isPositive then answer else (-answer)
      log.debug(s"UnPacked BigDecimal $signedAns")
      signedAns

    }
  }
}
