package com.odenzo.xrpl.bincodec.scodecs

import com.odenzo.xrpl.models.data.models.fields.ids.FieldId
import com.tersesystems.blindsight.LoggerFactory
import scodec.{Attempt, Codec, DecodeResult}
import scodec.bits.{BitVector, ByteVector, bin, hex}
import com.odenzo.xrpl.models.scodecs.FieldIdScodec.*
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.models.scodecs.FieldIdScodec

import scala.util.Random

/** Inidivudual Codecs Work, need a Combinator */
class FieldIdScodecTest extends munit.FunSuite with MyLogging {

  private val log                                                            = LoggerFactory.getLogger
  private val zero4                                                          = BitVector.low(4)
  val generalCodec: Codec[FieldId]                                           = FieldIdScodec.xrpfieldid
  def roundTrip(codec: Codec[FieldId], fieldId: FieldId, fullRaw: BitVector) = {
    val generalEncoded     = generalCodec.encode(fieldId).require
    val encoded: BitVector = codec.encode(fieldId).require
    assertEquals(encoded, generalEncoded)
    assertEquals(encoded, fullRaw)
    log.info(s"Encoded Field: ${generalEncoded.toBin}")

    val decoded: FieldId = generalCodec.decode(fullRaw).require.value
    log.info(s"Decoded FieldId: $decoded (DT: ${decoded.dataTypeCodeAsInt}, FT: ${decoded.fieldCodeAsInt})")
  }

  test("Smal Type Small Field") {
    val codec: Codec[FieldId] = FieldIdScodec.smallTypeAndSmallField
    val dt                    = bin"0010"
    val ft                    = bin"0100"
    val id                    = FieldId(dt, ft)
    val fullRaw               = dt ++ ft

    roundTrip(codec, fieldId = id, fullRaw = fullRaw)

  }

  test("Small Field Big Type") {
    val codec: Codec[FieldId] = FieldIdScodec.smallFieldAndType
    val dt                    = bin"00010011"
    val ft                    = bin"0001"
    val id                    = FieldId(dt, ft)
    val fullRaw               = zero4 ++ ft ++ dt

    roundTrip(codec, fieldId = id, fullRaw = fullRaw)

  }

  test("Small Type Big Field") {
    val codec: Codec[FieldId] = FieldIdScodec.smallTypeAndField
    val ft                    = bin"00100001"
    val dt                    = bin"0010"
    val id                    = FieldId(dt, ft)
    val fullRaw               = dt ++ zero4 ++ ft

    roundTrip(codec, fieldId = id, fullRaw = fullRaw)
  }

  test("Big Type Big Field") {
    val codec: Codec[FieldId] = FieldIdScodec.typeAndField
    val ft                    = bin"00010000"
    val dt                    = bin"00010000"
    val id                    = FieldId(dt, ft)
    val fullRaw               = zero4 ++ zero4 ++ dt ++ ft

    roundTrip(codec, fieldId = id, fullRaw = fullRaw)
  }

}
// Some Property Testing

def randomBits(size: Int): BitVector = ByteVector(Random.nextBytes(size / 8 + 1)).bits.take(size)
def randomByte                       = randomBits(8)
def randomNibble                     = randomBits(4)

//  test("Round Small Small") {
//    for {
//      i   <- 1 to 15
//      j   <- 1 to 15
//      bits = roundTripFieldId(i, j)
//      _    = bits.size shouldEqual 8
//    } yield (i, j)
//  }
//
//  test("Round Small Medium") {
//    for {
//      i   <- 1 to 15
//      j   <- 16 to 255
//      bits = roundTripFieldId(i, j)
//      _    = bits.size shouldEqual 16
//    } yield (i, j)
//  }
//
//  test("Round Medium Small") {
//    for {
//      i   <- 16 to 255
//      j   <- 1 to 15
//      bits = roundTripFieldId(i, j)
//      _    = bits.size shouldEqual 16
//    } yield (i, j)
//  }
//
//  test("Round Big Big") {
//    for {
//      i   <- 16 to 255
//      j   <- 16 to 255
//      bits = roundTripFieldId(i, j)
//      _    = bits.size shouldEqual 24
//    } yield (i, j)
//  }
//}
