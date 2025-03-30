package com.odenzo.xrpl.models.data.flags

import com.tersesystems.blindsight.LoggerFactory
import io.circe.{ Decoder, Encoder }
import scodec.bits.{ BitVector, ByteVector }

import scala.util.Try

case class BitMaskFlags(bits: BitVector)

object BitMaskFlags {

  private val log = LoggerFactory.getLogger

  /**
    * Creates a 64 bit mask by default. Given size it will pad/mask to correct
    * length.
    */
  def fromPositiveLong(num: Long, sizeInBits: Long = 64): BitMaskFlags =
    log.debug(s"Number: $num $sizeInBits bits")
    assert(num >= 0 && num <= Math.pow(2.0, sizeInBits.toDouble))
    val bv = ByteVector.fromLong(num).bits
    BitMaskFlags(bv)

  given Encoder[BitMaskFlags] = Encoder.encodeLong.contramap(bitmask => bitmask.bits.toLong(false))
  given Decoder[BitMaskFlags] = Decoder.decodeLong.emapTry(l => Try(fromPositiveLong(l)))

}
