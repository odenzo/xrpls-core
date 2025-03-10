package com.odenzo.xrp.bincodec.scodecs

import cats.implicits.*
import com.odenzo.ripple.bincodec.testkit.ScodecTestHelpers
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.monetary
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, FiatValue, XrplCurrency, XrplStdCurrency }
import com.odenzo.xrpl.models.scodecs.{ AmountFiatValueScodecs, AmountScodecs, MetaData }

import com.tersesystems.blindsight.{ Logger, LoggerFactory }
import io.circe.Json
import io.circe.literal.*
import scodec.bits.*
import scodec.{ Codec, DecodeResult }
import spire.math.ULong

import java.util.UUID

/**
  * This is top level discriminate for MonetaryAmont/CurrencyAmonut. It
  * basically decides with its a Drop or FiatAmount which are already tested.
  * So, here we test the discriminator Codec and its widening.
  */
class AmountScodecTest extends munit.FunSuite with ScodecTestHelpers {
  given log: Logger = LoggerFactory.getLogger

  import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.*

  val codec: Codec[CurrencyAmount] = AmountScodecs.amountCodec
  import XrplCurrency.given
  test(" FiatAmount Encode") {
    val anAmountFieldCode           = MetaData.getFieldMetaData("Amount").fieldId
    val src                         = "12.13"
    val srcAsBigDecimal             = BigDecimal.exact(src)
    val fiatValue: FiatValue        = FiatValue(src)
    val fiatValueBits               = AmountFiatValueScodecs.fiatValueCodec.encode(fiatValue).require.padLeft(64).set(1)
    log.info(s"Expecting Fiat Value: $fiatValueBits")
    val v: FiatAmount               = FiatAmount(fiatValue, XrplStdCurrency.USD.currency, AccountAddress.GENESIS)
    val expectedCurrency: BitVector = XrplStdCurrency.USD.currency.asBits
    val vPacked                     = FiatValue.fiatValueOps.packBigDecimal(fiatValue)
    assertEquals(vPacked._1, srcAsBigDecimal.signum >= 0, s"Sign Bit Should be the same as the src $src")

    log.info(s"Test Encoding $v")
    log.info(s"FiatValue Pacled: $vPacked")
    val res: BitVector   = codec.encode(v).require
    val totalLenShouldBe = 64 + 160 + 160
    // assertEquals(res.size, totalLenShouldBe.toLong, "Total Length Wrong")
    assertEquals(res.take(64).toHex, fiatValueBits.toHex, "First FiatValue should match encoded")
    assertEquals(res.drop(64).take(160).toHex, expectedCurrency.toHex, "Expected the Currency component to match")
    val issuer           = res.drop(64).drop(160)
    log.info(s"Issuer $issuer")
    log.debug(s"Encoded Fiat: ${res}") // This is getting fieldId and checksumed?
    log.debug(s"Res")
    // 40 bit differemce, 5 bytes. Although it seems totally ducled, Genesis.Address looks like zero

  }

  test(" FiatAmount Decode") {

    val src                    = "12.13"
    val srcAsBigDecimal        = BigDecimal.exact(src)
    val fiatValue: FiatValue   = FiatValue(src)
    val issuerAddress          = AccountAddress.GENESIS
    log.info(s"Isser Address: $issuerAddress")
    val issuer: AccountAddress = AccountAddress.GENESIS
    log.info(s"Issuer AccountId: $issuer")
    val v: FiatAmount          = FiatAmount(fiatValue, XrplStdCurrency.USD.currency, issuer)
    log.info(s"ENcoding Amount to Decode Later (with Full Address of 200 bits): $v")
    val vPacked                = FiatValue.fiatValueOps.packBigDecimal(fiatValue)
    assertEquals(vPacked._1, srcAsBigDecimal.signum >= 0, s"Sign Bit Should be the same as the src $src")

    log.info(s"Test Encoding $v")
    log.info(s"FiatValue Pacled: $vPacked")
    val res: BitVector       = codec.encode(v).require
    log.info(s"Full Encoded Fiat Amount: $res")
    assertEquals(res.size, 384L, "All encoded FiatAmounts should be 384 bits")
    log.info(s"Test Decoding ${res}")
    val back: CurrencyAmount = codec.decode(res).require.value
    val backFiat             = back match
      case v @ FiatAmount(amount, currency, issuer) => v
      case monetary.CurrencyAmount.Drops(amount)    => throw IllegalArgumentException("Got Drops expected Fiat")
    log.info(s"Src: $v. Encoded: $res => Decoded: $back")

    assertEquals(backFiat.amount, v.amount)
    assertEquals(backFiat.currency.asBitVector.toHex, v.currency.asBitVector.toHex)
    assertEquals(backFiat.issuer.asBits.toHex, v.issuer.asBits.toHex)

  }

}

//import cats.implicits._
//import scodec.bits._
//import com.odenzo.ripple.bincodec.OTestSpec
//import com.odenzo.ripple.bincodec.models.{ISOCurrency, XRPLAmount, XRPLCurrency, XRPLDrops}
//import io.circe.Json
//import org.scalatest.Assertion
//import spire.math.ULong
//import io.circe.literal._
//import scodec.DecodeResult
//
//class AmountScodecsTest extends OTestSpec with AmountScodecs {
//
//  implicit val log = true
//
//  val x = "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59"
//
////  test("XRP Encode") {
////    roundTripScodec(xrplAmount, XRPLDrops(12))
////  }
//
//  test("Currency") {
//    // Not sure this is exactly aligned  This should be fourty characters exactly, may be slidden by 1 byte
//    val nzd: ByteVector = hex"0000000000000000000000004e5a440000000000"
//    val res             = xrplCurrency.decode(nzd.bits).require
//    scribe.info(s"Result: $res")
//  }
//
//  test("Fiat Amount Doesnt Die") {
//    val fixture: Seq[String] = Seq(
//      "10",
//      "100",
//      "100.23456",
//      "0.12345",
//      "0000.0012345600",
//      "0000.12345",
//      "0000.123450000"
//    )
//
//  }
//
//  // Is the 0 value just for XRP, failing needed to double check
//  private val fixture =
//    json"""
//      [
//       { "bin": "8000000000000000" , "value": "0" }  ,
//       { "bin": "D4C38D7EA4C68000" , "value": "10" }
//      ]
//    """
//
//  test("Fixture") {
//
//    val f =
//      hex"110072220002000025000000EF55C6A2521BBCCF13282C4FFEBC00D47BBA18C6CE5F5E4E0EFC3E3FCE364BAFC6B862800000000000000000000000000000000000000055534400000000000000000000000000000000000000000000000001668000000000000000000000000000000000000000555344000000000036D16F18B3AAC1868C1E3E8FA8EB7DDFD8ECCCAC67D4C38D7EA4C680000000000000000000000000005553440000000000E14829DB4C6419A8EFCAC1EC21D891A1A4339871"
//
//    val zeroField: ByteVector =
//      hex"110072220002000025000000EF55C6A2521BBCCF13282C4FFEBC00D47BBA18C6CE5F5E4E0EFC3E3FCE364BAFC6B862800000000000000000000000000000000000000055534400000000000000000000000000000000000000000000000001668000000000000000000000000000000000000000555344000000000036D16F18B3AAC1868C1E3E8FA8EB7DDFD8ECCCAC67D4C38D7EA4C680000000000000000000000000005553440000000000E14829DB4C6419A8EFCAC1EC21D891A1A4339871"
//
//    val lastField =
//      hex"""
//             110072220002000025000000EF55C6A2521BBCCF13282C4FFEBC00D47BBA18C6CE5F5E4E0EFC3E3FCE364BAFC6B8
//             62800000000000000000000000000
//             0000000000000555344000000000
//             00000000000000000000000000000000000000001
//             668000000000000000000000000000
//             0000000000005553440000000000
//             36D16F18B3AAC1868C1E3E8FA8EB7DDFD8ECCCAC
//             67
//             D4C38D7EA4
//             C680000000000000000000000000005553440000000000E14829DB4C6419A8EFCAC1EC21D891A1A4339871"""
//
//  }
//
//  val justAmount =
//    json"""{
//            "currency" : "USD",
//            "value" : "10",
//            "issuer" : "rMYBVwiY95QyUnCeuBQA1D47kXA9zuoBui"
//          }"""
//
//  val nzAmount =
//    json"""{
//            "value" : "555.666",
//            "currency" : "NZD",
//            "issuer" : "rwjH4461qQTa4fksY8doTLRdqKxDyd5nsi"
//          }
//    """
//
//  test("NZ Amount") {
//    val iou = decodeJson[XRPLAmount](nzAmount)
//    scribe.debug(s"XPLAmount: $iou")
//    val iouEnc: BitVector = xrplAmount.encode(iou).require
//    scribe.info(s"Encoded $iouEnc")
//    val amtEnc: DecodeResult[XRPLAmount] = xrplAmount.decode(iouEnc).require
//    iou shouldEqual amtEnc.value
//  }
//
//  test("Fiat Package") {
//
//    val issuerStr = "rMYBVwiY95QyUnCeuBQA1D47kXA9zuoBui"
//    val i2Str     = "rMBzp8CgpE441cp5PVyA9rpVV7oT8hP3ys"
//    val expected = "67" +
//      "D4C38D7EA4C68000" +
//      "0000000000000000000000005553440000000000" + // Currency
//      "E14829DB4C6419A8EFCAC1EC21D891A1A4339871"   // Issuer with no VL Encoding
//
//    val fieldExpected: ByteVector = hex"67"
//    val amountExpected            = hex"D4C38D7EA4C68000"
//    val currencyExpected          = hex"0000000000000000000000005553440000000000"
//    val issuerExpected            = hex"E14829DB4C6419A8EFCAC1EC21D891A1A4339871" // Issuer with no VL Encoding
//
//    roundTripScodec(currencyExpected.bits, ISOCurrency("USD"), xrplCurrency)(true)
////
////    val amount: String = ("10")
////    val amountBytes = roundTripFromEncode(IssuedAmountCodec.encodeFiatValue(amount))
////    val amountHex: String = amountBytes.toHex
////    scribe.info(s"Amount: $amount => $amountHex")
////
////    val issuer = (issuerStr)
////    val issuerBytes = getOrLog(AccountIdCodecs.encodeAccountNoVL(issuer))
////    scribe.debug(s"Issuer [$issuer] Len ${issuerBytes.length} : ${issuerBytes.toHex.toUpperCase}")
////
////    hex shouldEqual currencyExpected
////    amountHex shouldEqual amountExpected
//  }
//
//  test("XRP") {
//    // XRP Binary needs to have the 64th bit set no matter what
//    val min: ULong = ULong(0)
//    val max: ULong = ULong.fromBigInt(spire.math.pow(BigInt(10), BigInt(17)))
//    scribe.info(s"Min - Max $min $max")
//
//    t(max)
//    t(max - ULong(1))
//    t(ULong(370000000))
//
//    def t(v: ULong) = {
//      val drops         = XRPLDrops(v.toLong)
//      val jsonV: String = v.toString()
//      val json: Json    = Json.fromString(jsonV)
//      scribe.info(s"From $v Sending JSON: ${json.noSpaces}")
//      roundTripFromEncode(drops, xrplAmount)(true)
//
//    }
//
//  }
//
//  test("Currency Encoding") {
//
//    // @todo Not sure that 1 or 2 character codes are acceptable
//    val passFail = List(
//      ("XRP", true), // This is special case
//      ("mew", true),
//      ("FOO", true),
//      ("BARR", false),
//      ("~~~", false), // Not a ripple ASCII char
//      ("[^]", true)
////      ("AA".padTo(20, 'F'), false),
////      ("00".padTo(20, 'F'), true),
////      ("01".padTo(20, 'F'), true) // Legacy case
//    )
//
//    passFail.foreach {
//      case (v, exp) =>
//        scribe.debug(s"Testing Currency: $v which should be $exp")
//        encode(v, exp)
//    }
//
//    def encode(v: String, expectedOK: Boolean): Assertion = {
//      val curr = ISOCurrency(v)
//      val res  = xrplCurrency.encode(curr)
//      res.isSuccessful shouldEqual expectedOK
//    }
//
//  }
//
//}
