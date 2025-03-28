package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.binary.FixedSizeBinary
import io.circe.{ Decoder, Encoder }
import scodec.bits.BitVector

/** 32-bit unsigned. TODO: Unsure the JSON format for this? Hex for now */
opaque type SourceTag = BitVector

val tc: FixedSizeBinary[SourceTag] = new FixedSizeBinary[SourceTag](32) {
  def fromBits(bits: BitVector): SourceTag = bits
  def toBits(a: SourceTag): BitVector      = a
}
given FixedSizeBinary[SourceTag]   = tc

object SourceTag {
  given encoder: Encoder[SourceTag] = tc.encoderHex
  given decoder: Decoder[SourceTag] = tc.decoderHex

  given (using FixedSizeBinary[SourceTag]): Encoder[SourceTag] =
    Encoder.encodeString.contramap(_.toString)

}
