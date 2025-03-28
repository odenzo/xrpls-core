package com.odenzo.xrpl.models.data.models.fields.ids

import io.circe
import io.circe.Codec.{ AsObject, codecForValidated }
import scodec.bits.*
import scodec.bits.ByteOrdering.BigEndian
import scodec.codecs.{ uint4, uint8 }
import scodec.{ Codec, Decoder, Encoder }

import scala.math.Ordered.orderingToOrdered

/**
  * https://xrpl.org/docs/references/protocol/binary-format#field-ids FieldId
  * contains a TypeCode and FieldCode. FieldCode used for sorting. Both dataType
  * and fieldCode are uint4 or uint8 as BitVectors.
  *
  * We actually create FieldId for all the possible fields from configuration at
  * startip. So, it comes down to re-using. Thus we computer a bunch of stuff
  * early and eagerly
  *
  * Note: We actually want to codec this on ??? the packed binary to Hex? We
  * just want it to get FieldId from the MetaData and ScodecResolver just needs
  * a round Trip?
  */
case class FieldId(dataTypeCode: BitVector, fieldCode: BitVector) extends Ordered[FieldId] {

  val dataTypeCodeAsInt: Int = dataTypeCode.toInt(signed = false, ordering = BigEndian)
  val fieldCodeAsInt: Int    = fieldCode.toInt(signed = false)

  /**
    * Packs to full binary which sometimes require padding. Total length varies.
    */
  val packedBinary: BitVector = {
    (fieldCode.size, dataTypeCode.size) match
      case (4, 4) => dataTypeCode ++ fieldCode
      case (4, 8) => fieldCode ++ bin"0000" ++ dataTypeCode // Caution, rthis is fucked?
      case (8, 4) => dataTypeCode ++ bin"0000" ++ fieldCode
      case (8, 8) => hex"00".bits ++ dataTypeCode ++ fieldCode
  }

  /**
    * Revisit this. It is basically the sort key for ordering fields in an XRPL
    * object
    */
  override def compare(that: FieldId): Int =
    dataTypeCode.compare(that.dataTypeCode) match
      case less if less < 0 => less
      case more if more > 0 => more
      case 0                => this.fieldCode.compare(that.fieldCode)
}

object FieldId:
  import scodec.codecs

  /*

/** Some logic duplication in models module too, try and unify */

case class FieldId(typeCode: Int, fieldCode: Int) {
  val adjustedTypeCode: Int = if (typeCode > 1000) 16 else typeCode
  val orderKey: (Int, Int)  = (this.typeCode, this.fieldCode)

}

object FieldId {

  def fromTypeAndField(tf: (Int, Int)): FieldId = (FieldId.apply _).tupled(tf)
  def fromFieldAndType(ft: (Int, Int)): FieldId = fromTypeAndField(ft.swap)

}
   */
  /**
    * This takes a signed int and creates 4 or 8 bits of normal encoding (not
    * 2s-complement)
    */
  inline private def toBinaryField(i: Int): BitVector =
    val bits = if i < 16 then uint4.encode(i) else uint8.encode(i)
    bits.require

  def fromFieldCodeAndDataTypeCode(fieldCode: Int, typeCode: Int): FieldId = {
    (fieldCode < 16, typeCode < 16) match
      case (false, true) => FieldId(toBinaryField(fieldCode), toBinaryField(typeCode))
      case _             => FieldId(toBinaryField(typeCode), toBinaryField(fieldCode))
  }

  @deprecated
  def fieldCode(bv: ByteVector): FieldCode = {
    val fieldCodeBits: BitVector = bv.size match {
      case 1 => bv.bits.drop(4)
      case 2 => if bv.head == 0x0 then bv.bits.drop(4).take(4) else bv.drop(1).bits
      case 3 => bv.drop(2).bits
    }
    FieldCode.fromBitVector(fieldCodeBits)
  }

  @deprecated
  def typeCode(bv: ByteVector): DataTypeCode = {
    val bits: BitVector = bv.size match {
      case 1 => bv.bits.take(4)
      case 2 => if bv.head == 0x0 then bv.bits.drop(8) else bv.bits.take(4)
      case 3 => bv.drop(1).take(1).bits
    }
    DataTypeCode.fromBitVector(bits)
  }

end FieldId
