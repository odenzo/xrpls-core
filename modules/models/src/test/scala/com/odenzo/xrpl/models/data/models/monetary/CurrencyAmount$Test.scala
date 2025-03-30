package com.odenzo.xrpl.models.data.monetary

//


import com.odenzo.xrpl.models.data.atoms.AccountAddress
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.{ Drops, FiatAmount, given }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Decoder.Result
import io.circe.literal.json
import io.circe.syntax.*
import io.circe.{ Encoder, Json }
import scodec.bits.{ BitVector, hex }

import java.util.UUID

class CurrencyAmount$Test extends munit.FunSuite {

  private val log = LoggerFactory.getLogger

  val fiatStd = json""" {
  "value" : "1000",
  "currency" : "QQQ",
  "issuer" : "rsmxMuiX3GBcyDqjQoUPTHuauMAdNWJA3X"
}"""
  test("Encoding Drops") {

    val encoded: Json = Drops(50).asJson
    assertEquals(encoded, Json.fromString("50"))
//    val amt: CurrencyAmount = Drops(55)
//    amt.asJson shouldEqual Json.fromString("55")

  }
//
//  test("Test Conversion") {
//    assertEquals(Drops(1) shouldEqual CurrencyAmount.fromXrp("0.000001")
//  }

  import XrplCurrency.given
  import XrplStdCurrency.given
  test("ISO Encoding FiatAmount") {
    val currency: XrplCurrency = XrplStdCurrency.NZD.currency
    val amount: FiatAmount     =
      CurrencyAmount.FiatAmount(FiatValue("666543.22"), currency, AccountAddress.GENESIS)
    {
      val json = currency.asJson
      log.info(s"Currency Json: ${json.spaces4}")
      log.info(s"CurrencyStd Json: ${XrplStdCurrency.NZD.currency.asJson.spaces4}")
      assertEquals(json, XrplStdCurrency.NZD.currency.asJson)
    }
    {
      val json = amount.asJson
      log.info(s"Amount Json: ${json.spaces4}")

    }
  }

  def randomBits: BitVector = hex"FF".bits ++ BitVector.fromUUID(UUID.randomUUID()).take(160 - 8).padLeft(160 - 8)
  test("Encoding Custom FiatAmount") {
    val currency: XrplCurrency = XrplCurrency.fsb.fromBits(randomBits)
    val amount: FiatAmount     =
      CurrencyAmount.FiatAmount(FiatValue("666543"), currency, AccountAddress.GENESIS)
    {
      val json = currency.asJson
      log.info(s"Currency Json: ${json.spaces4}")
      assert(currency.asBits.size == 160)
      // assertEquals(json, XrplStdCurrency.NZD.asJson)
    }

    {
      val json = amount.asJson
      log.info(s"Amount Json: ${json.spaces4}")

    }

  }
  test("Decoding FiatAmount Std") {
    val res: Result[CurrencyAmount.FiatAmount] = fiatStd.as[FiatAmount]
    log.info(s"FiatStd: $fiatStd and Result: $res")
    assert(res.isRight)
    val fa: CurrencyAmount.FiatAmount          = res.fold(e => fail("Decoding Failure", e), v => identity(v))
    log.debug(s"FiatAmount: ${fa.asJson.spaces4}")
  }

// Status: Encoding seems to work OK.
  // Decoding stuck on a Long instead of looking for BigDecimal as a String I think
//  test("Decoding Drops") {
//    val drops: Result[Drops] = Json.fromString("66").as[Drops]
//    logger.info(s"Simple Drops $drops")
//    drops.right.value shouldEqual Drops(66)
//
//    val dropsCA: Result[CurrencyAmount] = Json.fromString("66").as[CurrencyAmount]
//    logger.info(s"Drops CAmount $dropsCA")
//    dropsCA.right.value shouldEqual Drops(66)
//
//  }

//  test("Decoding Fiat") {
//    val fiatObj = FiatAmount(BigDecimal(666), Script(Currency.NZD, AccountAddr("rGarbage")))
//    val sample  = """{
//                   |  "value" : "666",
//                   |  "currency" : "NZD",
//                   |  "issuer" : "rGarbage"
//                   |}""".stripMargin
//
//    val json: Json                             = parse(sample)
//    val fiat                                   = json.as[FiatAmount]
//    fiat.right.value shouldEqual fiatObj
//    val currencyAmount: Result[CurrencyAmount] = json.as[CurrencyAmount]
//    currencyAmount.right.value shouldEqual fiatObj
//
//  }
}
