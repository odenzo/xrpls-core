package com.odenzo.xrpl.models.data.models.fields.ids

import scodec.Codec
import scodec.bits.BitVector

/**
  * -https://xrpl.org/docs/references/protocol/binary-format#length-prefixing
  *   - aka Field LengthPrefix Some variables have a field len, which itself is
  *     of varying length.
  * Modeled as an Int for now, its really unsigned and will need a bytevector
  * eventually.
  */
opaque type FieldLen = Int

object FieldLen {
  def fromBitVector(bits: BitVector): FieldLen = bits.toInt(false)

  // Need a scodec to embedd in both JSON Encoder and a Scodec Decoder/Encoder.
  val scodec8: Codec[FieldLen] = scodec.codecs.uint(8)
}

/* Notes,
 * - Field length is 1,2, or 3 bytes.
 * - Max Length of field is 918744
 * - When decoding, you can tell from the value of the first length byte whether there are 0, 1, or 2 additional length
 * bytes:
 *
 * - If the first length byte has a value of 192 or less, then that's the only length byte and it contains the exact
 * length of the field contents in bytes.
 * - If the first length byte has a value of 193 to 240, then there are two length bytes.
 * - If the first length byte has a value of 241 to 254, then there are three length bytes. */
