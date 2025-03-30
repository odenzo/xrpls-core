package com.odenzo.xrpl.models.ledgerids

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.atoms.LedgerHash
import com.odenzo.xrpl.models.data.atoms.hash256.*
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import com.tersesystems.blindsight.LoggerFactory
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.syntax.given
import io.circe.{ Codec, Json }
import scodec.bits.{ BitVector, ByteVector }
class LedgerHandleTest extends munit.FunSuite {

  given bvCodec: Codec[ByteVector] = CirceCodecUtils.hexCodec
  private val log                  = LoggerFactory.getLogger

  case class Rs(ledger: LedgerHandle) derives Codec.AsObject

  val constantDataOnly: Seq[(LedgerHandle, Int | String)] = List(
    LedgerHandle.closed -> "closed",
    LedgerHandle.validated -> "validated",
    LedgerHandle.current -> "current",
  )

  val data: Seq[(LedgerHandle, Int | String)] = List(
    LedgerIndex(12) -> 12,
    LedgerIndex(13) -> 13, // Will never round trip
    LedgerIndex(-1) -> -1, // Will never round trip
    LedgerIndex(0) -> 0, // Will never round trip
    LedgerHandle.closed -> "closed",
    LedgerHandle.validated -> "validated",
    LedgerHandle.current -> "current",
  )

  val dataAsJson: Seq[(LedgerHandle, Json)] = data.map((f: (LedgerHandle, Int | String)) =>
    val expectedJson = f._2 match
      case x: Int    => Json.fromInt(x)
      case x: String => Json.fromString(x)
    (f._1, expectedJson)
  )

  case class LedgerIndexI(ledger_index: LedgerHandle) derives Codec.AsObject
  case class LedgerCurrentIndexI(ledger_current_index: LedgerHandle) derives Codec.AsObject
  case class LedgerAliased(my_ledger: Option[LedgerHandle] = None, ledger_index: LedgerHandle) derives Codec.AsObject

  test("LedgerHandle Decoding - In ledger_index ") {
    // Will never round trip)
    data.foreach { (d: LedgerHandle, expected: Int | String) =>
      val json    = LedgerIndexI(d).asJson
      log.debug(s"Encoded $d to ledger_index ${json.noSpaces}")
      val decoded = json.as[LedgerIndexI]
      log.debug(s"$d => Decoded: $decoded")
      d match
        case LedgerHandle.LedgerIndex(i) => assert(decoded.isRight, s"Decoding of ${d} failed: $decoded")
        case constant                    => log.info(s"Constant: $constant cannot be decoded, only encoded $decoded")
    }
  }

  test("LedgerHandle Decoding - In ledger_current_index") {
    // Will never round trip)
    data.foreach { (d: LedgerHandle, expected: Int | String) =>
      val json    = LedgerCurrentIndexI(d).asJson
      log.debug(s"Encoded $d to ledger_current_index ${json.noSpaces}")
      val decoded = json.as[LedgerCurrentIndexI]
      log.debug(s"RESULT: $d => Decoded: $decoded")
      d match
        case LedgerHandle.LedgerIndex(i) => assert(decoded.isRight, s"Decoding of ${d} failed: $decoded")
        case constant                    => log.info(s"Constant: $constant cannot be decoded, only encoded")
    }
  }

  test("Encode All Good LedgerHandleType") {
    dataAsJson.foreach { (data, expected) =>
      log.debug(s"Testing $data  expecting ${expected.noSpaces}")
      val encoded = data.asJson
      assertEquals(encoded, expected)
    }
  }
  test("Ledger Handle Endcoding of All EnumVals") {
    import Hash256.given
    val hash256: Hash256 = Hash256.validatedBits(BitVector.high(32))
    val ledgerHash       = LedgerHash(hash256)
    List(LedgerHandle.validated, LedgerHandle.current, LedgerHandle.closed, LedgerHandle.LedgerIndex(10000)).foreach {
      (v: LedgerHandle) => println(v.asJson)
    }

    println(LedgerIndex(2999).asJson)
    println(ledgerHash.asJson)

  }

  case class LedgerIndexObj(ledgerIndex: LedgerHandle.LedgerIndex, context: String) derives ConfiguredCodec
  object LedgerIndexObj  {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }
  case class LedgerHandleObj(ledgerIndex: LedgerHandle, context: String) derives ConfiguredCodec
  object LedgerHandleObj {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  case class LedgerHandlerWithOddName(notLedgerIndex: LedgerHandle, context: String) derives ConfiguredCodec

  object LedgerHandlerWithOddName {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  import LedgerHandleObj.derived$ConfiguredCodec
//
//  def roundTripLedgerIndex(t: LedgerHandle, fieldName: String) = {
//    // Encoding
//    val res: Json = t.asJson
//    log.info("Result Json: " + res.noSpaces)
//
//    val aliasedField       = JsonObject(fieldName -> res)
//    val ledgerIndexOnly    = JsonObject("ledger_index" -> res)
//    val ledgerCurrentOnly  = JsonObject("ledger_current_index" -> res)
//    List(aliasedField, ledgerIndexOnly, ledgerCurrentOnly)
//    val decoded: Result[T] = res.as[T]
//    decoded match {
//      case Left(value)  => log.warn(s"Could not decode ${res.noSpaces}", value)
//      case Right(value) => log.debug(s"Decoded ${pprint.apply(value)}")
//    }
//
//    assert(decoded.isRight)
//
//  }

//  test("LedgerIndex") {
//    roundTripLedgerIndex(LedgerIndexObj(LedgerIndex(12), "With LedgerIndex"))
//  }
//
//  test("LedgerHandle Numeric") {
//    roundTripLedgerIndex(LedgerHandleObj(LedgerIndex(12), "Handle With Numeric Index"))
//  }
//  test("Constant") {
//    roundTripLedgerIndex(LedgerHandleObj(LedgerHandle.validated, "Constant"))
//    roundTripLedgerIndex(LedgerHandlerWithOddName(LedgerHandle.validated, "Constant in other field"))
//  }
  test("Dummy with LedgerHandle Constant") {
    val json = LedgerHandleObj(LedgerHandle.current, "Handle With Constat").asJson
    val res  = json.as[LedgerHandleObj]
    log.info("Result: " + res)
  }

  test("DummyLabel with LedgerHandle Numeric") {
    val res: Json = LedgerHandlerWithOddName(LedgerIndex(12), "Handle With Numeric Index")
      .asJsonObject.add("ledger_index", Json.fromLong(2)).asJson
    log.info("Result Json: " + res)
    val dec       = res.as[LedgerHandlerWithOddName]
    assert(dec.isRight)
    log.info(s"Decoded: ${pprint.apply(dec)}")
  }

}
