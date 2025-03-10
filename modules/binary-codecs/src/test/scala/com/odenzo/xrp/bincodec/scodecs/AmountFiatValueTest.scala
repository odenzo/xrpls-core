package com.odenzo.xrp.bincodec.scodecs

import cats.implicits.*
import com.odenzo.ripple.bincodec.testkit.ScodecTestHelpers

import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, FiatValue, XrplCurrency, XrplStdCurrency }
import com.odenzo.xrpl.models.scodecs.AmountFiatValueScodecs

import com.tersesystems.blindsight.{ Logger, LoggerFactory }
import io.circe.Json
import io.circe.literal.*
import scodec.bits.*
import scodec.{ Codec, DecodeResult }
import spire.math.ULong

import java.util.UUID

/**
  * Test the numeric amount for fiat value, seperate from th4 Currency and
  * Address
  */
class AmountFiatValueTest extends munit.FunSuite with ScodecTestHelpers {
  given log: Logger = LoggerFactory.getLogger

  import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.*

  val codec: Codec[FiatValue] = AmountFiatValueScodecs.fiatValueCodec

  test(" Fiat RoundTrip - Direct") {
    // Fiat Value should always be 64 bits, this is currently 63 bits since we are not putting the First Bit Back
    // Which should always be '1' but is done throut Amount discriminator normally
    val src          = "12.13"
    val v: FiatValue = FiatValue(src)

    log.info(s"Test Encoding $v")
    val res: BitVector = codec.encode(v).require
    log.info(s"Test Decoding ${res}")
    val back           = codec.decode(res).require.value
    log.info(s"Src: $v. Encoded: $res => Decoded: $back")

    assertEquals(back, v)

  }

}

/* // test("Conformance") { // // Need to make a decent conformance test set // // // val fixture =
 * // """ // |[ // | {"bin":"94838D7EA4C68000" ,"mant": "00038D7EA4C68000" , "exp": -15, "value": "-1.0" }, // | // |
 * {"bin":"8000000000000000" ,"mant": "0000000000000000" , "exp": -15, "value": "0.0" }, // | // |
 * {"bin":"D4C3F28CB71571C7" ,"mant": "0003F28CB71571C7" , "exp": -14, "value": "11.11111111111111" } // |] //
 * """.stripMargin // // } */
