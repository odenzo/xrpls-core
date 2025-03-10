package com.odenzo.xrpl.models.data.models.monetary

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.{ Decoder, Encoder }
import jdk.jfr
import scodec.bits.BitVector

/**
  * The conversion rate from two FiatAmounts of the same currency when they
  * "Ripple" as an integer in the implied ratio 1,000,000,000. The value 0 is
  * equivalent to 1 billion, or face value.
  *
  * @param v
  *   See
  */
case class Quality(v: Double)

/** This is encoded as a number, which no quotes */
object Quality {

  given Encoder[Quality] = Encoder.encodeDouble.contramap[Quality](_.v)
  given Decoder[Quality] = Decoder.decodeDouble.map(v => Quality(v))
}
