package com.odenzo.xrpl.models.data.models.fields.ids

import scodec.bits.{ BitVector, ByteVector }

/**
  * Each field has a field code, which is used to sort fields that have the same
  * type as one another, with lower codes sorting first. These fields are
  * defined in sfields.macro. Field codes are reused for fields of different
  * field types, but fields of the same type never have the same field code.
  * When you combine the type code with the field code, you get the field's
  * unique Field ID.
  *   - https://xrpl.org/docs/references/protocol/binary-format#type-codes
  *     - https://github.com/XRPLF/rippled/blob/master/include/xrpl/protocol/detail/sfields.macro
  *   - We need an ordering to sort on the FieldCode. Actually FieldId in a way,
  *     order TypeCode ASC, FieldCode ASC Is there a ORDER[BitVector]
  */
opaque type FieldCode = BitVector

object FieldCode:
  def fromBitVector(bits: BitVector): FieldCode = bits: FieldCode
  def fromByteVector(bv: ByteVector): FieldCode = bv.bits

  given bitOrdering: Ordering[FieldCode] = new Ordering[FieldCode] {
    override def compare(x: FieldCode, y: FieldCode): Int =
      x.compare(y) // Scodec BitVector compare
  }
