package com.odenzo.xrpl.models.scodecs

import cats.*
import cats.data.*
import cats.implicits.*
import com.odenzo.xrpl.models.data.models.fields.ids.FieldId
import scodec.bits.{ BitVector, ByteVector, bin, hex }
import scodec.codecs.*
import scodec.{ Codec, Encoder }

/** Each field has a FieldId header that tells us what it is. Also used to sort. */
object FieldIdScodec {

//  def encode(typeCode: Int, fieldCode: Int): BitVector = {
//    FieldIdScodec.xrpfieldid.encode(FieldId(typeCode, fieldCode)).require
//  }

  val typeAndField: Codec[FieldId] =
    (constant(hex"00") ~> bits(8) :: bits(8))
      .withContext("FieldId").xmap(
        (t, f) => FieldId(t, f),
        (b: FieldId) => (b.dataTypeCode, b.fieldCode),
      )

  val smallFieldAndType: Codec[FieldId] =
    (constant(bin"0000") ~> bits(4) :: bits(8)).xmap(
      (f, t) => FieldId(t, f),
      (b: FieldId) => (b.fieldCode, b.dataTypeCode),
    )

  val smallTypeAndField: Codec[FieldId] =
    ((bits(4) <~ constant(bin"0000")) :: bits(8)).xmap(
      (t, f) => FieldId(t, f),
      (id: FieldId) => (id.dataTypeCode, id.fieldCode),
    )

  val smallTypeAndSmallField: Codec[FieldId] =
    (bits(4) :: bits(4)).xmap(
      (t, f) => FieldId(t, f),
      (id: FieldId) => (id.dataTypeCode, id.fieldCode),
    )

  private val orderedChoice = List(typeAndField, smallTypeAndField, smallFieldAndType, smallTypeAndSmallField)

  given xrpfieldid: Codec[FieldId] = { // Can Peek
    Codec[FieldId](encoder = choice(orderedChoice.reverse*), decoder = choice(orderedChoice*))
  }

  /**
    * Some things, like AccountAddress have a calculate 4 byte checksum
    * appended. This requires taking the fieldId and the body and the next codec
    * (in binary) and appending the result of checksumming those. Haven't
    * thought this through and need to align with the sniffer which routes to
    * scodec by either discruminating on fieldId or doing a peek etc,
    */
  def checksumCodec(fieldId: FieldId): Codec[BitVector] = scodec.codecs.bits(4 * 8)

}
