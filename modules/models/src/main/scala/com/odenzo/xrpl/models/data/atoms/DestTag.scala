package com.odenzo.xrpl.models.data.atoms

import com.odenzo.xrpl.common.binary.FixedSizeBinary
import io.circe.{ Decoder, Encoder }
import scodec.bits.BitVector

/** 32-bit unsigned. */
opaque type DestTag = BitVector

object DestTag {

    given tc: FixedSizeBinary[DestTag] = new FixedSizeBinary[DestTag](32) {
      def fromBits(bits: BitVector): DestTag = bits
      def toBits(a: DestTag): BitVector = a
    }


    given encoder: Encoder[DestTag] = tc.encoderHex
    given decoder: Decoder[DestTag] = tc.decoderHex



}
