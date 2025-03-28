package com.odenzo.xrpl.models.data.models.fields.ids

import scodec.bits.BitVector

/**
  * Clarify TypeCode vs TypePrefix vs Ripple Standard Types 9e.g. UINt8,
  * StObjext) and their code. There is a predefined list of typecodes. Another
  * lack of discipline consistency.
  */
opaque type DataTypeCode = BitVector

/** TypeCode < 16 into 4 non-zero bits TypeCOde >=16 */
object DataTypeCode:
  def fromBitVector(bits: BitVector)(using o: Ordering[DataTypeCode]): DataTypeCode = bits

  object bitOrdering extends Ordering[DataTypeCode] {
    def compare(x: DataTypeCode, y: DataTypeCode): Int = (x: BitVector).compare(y: BitVector)
  }
  given Ordering[DataTypeCode] = bitOrdering

  extension (tc: DataTypeCode) def bits: BitVector = tc
