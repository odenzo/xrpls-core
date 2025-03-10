package com.odenzo.xrp.bincodec.scodecs

import cats.implicits.*
import com.odenzo.ripple.bincodec.testkit.ScodecTestHelpers
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, XrplCurrency }
import com.odenzo.xrpl.models.scodecs.AmountDropsScodecs

import com.tersesystems.blindsight.{ Logger, LoggerFactory }
import io.circe.Json
import io.circe.literal.*
import scodec.bits.*
import scodec.{ Codec, DecodeResult }
import spire.math.ULong

import java.util.UUID

/** Test the */
class AmountDropsTest extends munit.FunSuite with ScodecTestHelpers {
  given log: Logger = LoggerFactory.getLogger

  import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.*

  val codec: Codec[Drops] = scodeclogger(AmountDropsScodecs.xrpDropsCodec)

  test("Drops RoundTrip - Direct") {

    val drops = CurrencyAmount.xrp(555)

    val res: BitVector = codec.encode(drops).require
    val back           = codec.decode(res).require.value
    log.info(s"Src: $drops. Encoded: $res => Decoded: $back")

    assertEquals(back.amount, drops.amount)

  }

}
