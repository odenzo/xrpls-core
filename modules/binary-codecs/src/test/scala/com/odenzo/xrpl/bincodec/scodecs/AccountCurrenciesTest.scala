package com.odenzo.xrpl.bincodec.scodecs

import com.odenzo.xrpl.models.data.models.monetary.{ XrplCurrency, XrplStdCurrency }

class AccountCurrenciesTest {

  import com.odenzo.xrpl.models.data.models.atoms.{ *, given }
  import cats.implicits.*
  import com.odenzo.ripple.bincodec.testkit.ScodecTestHelpers
  import com.odenzo.xrpl.models.scodecs.AmountCurrencyScodecs
  import com.tersesystems.blindsight.{ Logger, LoggerFactory }
  import io.circe.Json
  import io.circe.literal.*
  import scodec.{ Attempt, Codec, DecodeResult }
  import scodec.bits.*

  import java.util.UUID

  class AmountCurrencyTest extends munit.FunSuite with ScodecTestHelpers {
    given log: Logger = LoggerFactory.getLogger

    //
    //  val x = "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59"
    //
    ////  test("XRP Encode") {
    ////    roundTripScodec(xrplAmount, XRPLDrops(12))
    ////  }
    val codec: Codec[XrplCurrency] = scodeclogger(AmountCurrencyScodecs.bimodalCurrencyCodec)

    test("ISO Currency Decoder") {
      // Not sure this is exactly aligned  This should be fourty characters exactly, may be slidden by 1 byte
      val nzd: XrplCurrency               = XrplStdCurrency.NZD.currency
      log.info(s"NZD BitSize: ${nzd} about to be decoded")
      val res: DecodeResult[XrplCurrency] = codec.decode(nzd.asBits).require
      log.info(s"Result of Decoding ISO NZD to Currency: $res, going to encode")
      assertEquals(res.value.asBits, nzd.asBits)
    }

    test("ISO Currency Encoder") {
      val nzd: XrplCurrency = XrplStdCurrency.NZD.currency
      log.info(s"NZD BitSize: ${nzd} about to be encoded")
      val res: BitVector    = codec.encode(nzd).require
      log.info(s"Result: $res ")

    }

    test("Custom Currency Encoder ") {
      // Not sure this is exactly aligned  This should be fourty characters exactly, may be slidden by 1 byte
      val custom: BitVector = hex"00".bits ++ BitVector.fromUUID(UUID.randomUUID()).padLeft(160).drop(8)
      log.info(s"Custom Currency value: ${custom.toHex} (size= ${custom.size}")

      val toEncode: XrplCurrency = XrplCurrency.fsb.fromBits(custom)

      log.info(s"Trying to Encode: ${toEncode}")
      val back: BitVector = codec.encode(toEncode).require
      assertEquals(back.size, 160L, "Result size was not 160L")
      // This should actually round trip the bits
      assertEquals(back.toHex, custom.toHex, "Encoding Custom Currency should no change the bits")
      //    val squareOne: XrplCurrency         = codec.decode(back).require.value
      //    log.info(s"Result: $res and Back: $back => ${squareOne.asBitVector.toHex}")

    }

    test("Custom Currency Decoder ") {
      // Not sure this is exactly aligned  This should be fourty characters exactly, may be slidden by 1 byte
      val custom: BitVector = hex"00".bits ++ BitVector.fromUUID(UUID.randomUUID()).padLeft(160).drop(8)
      log.info(s"Custom Currency value: ${custom.toHex} (size= ${custom.size}")

      val res: DecodeResult[XrplCurrency] = codec.decode(custom).require
      log.info(s"Result of Decoding Custom Currency: $res")
      assertEquals(res.value.asBitVector.size, 160L)

    }
  }

  //  test("Fiat Amount Doesnt Die") {
  //    val fixture: Seq[String] = Seq(
  //      "10",
  //      "100",
  //      "100.23456",
  //      "0.12345",
  //      "0000.0012345600",
  //      "0000.12345",
  //      "0000.123450000",
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
  //  test("Conformance") {
  //    // Need to make a decent conformance test set
  //    //
  //    val fixture =
  //      """
  //        |[
  //        | {"bin":"94838D7EA4C68000" ,"mant": "00038D7EA4C68000" , "exp": -15, "value": "-1.0" },
  //        |
  //        | {"bin":"8000000000000000" ,"mant": "0000000000000000" , "exp": -15, "value": "0.0" },
  //        |
  //        | {"bin":"D4C3F28CB71571C7" ,"mant": "0003F28CB71571C7" , "exp": -14, "value": "11.11111111111111" }
  //        |]
  //      """.stripMargin
  //
  //  }
  //  test("Fixture") {
  //
  //    val f =
  // hex"110072220002000025000000EF55C6A2521BBCCF13282C4FFEBC00D47BBA18C6CE5F5E4E0EFC3E3FCE364BAFC6B862800000000000000000000000000000000000000055534400000000000000000000000000000000000000000000000001668000000000000000000000000000000000000000555344000000000036D16F18B3AAC1868C1E3E8FA8EB7DDFD8ECCCAC67D4C38D7EA4C680000000000000000000000000005553440000000000E14829DB4C6419A8EFCAC1EC21D891A1A4339871"
  //
  //    val zeroField: ByteVector =
  // hex"110072220002000025000000EF55C6A2521BBCCF13282C4FFEBC00D47BBA18C6CE5F5E4E0EFC3E3FCE364BAFC6B862800000000000000000000000000000000000000055534400000000000000000000000000000000000000000000000001668000000000000000000000000000000000000000555344000000000036D16F18B3AAC1868C1E3E8FA8EB7DDFD8ECCCAC67D4C38D7EA4C680000000000000000000000000005553440000000000E14829DB4C6419A8EFCAC1EC21D891A1A4339871"
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
  val justAmount =
    json"""{
              "currency" : "USD",
              "value" : "10",
              "issuer" : "rMYBVwiY95QyUnCeuBQA1D47kXA9zuoBui"
            }"""

  val nzAmount =
    json"""{
              "value" : "555.666",
              "currency" : "NZD",
              "issuer" : "rwjH4461qQTa4fksY8doTLRdqKxDyd5nsi"
            }
      """
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
  // }

}
