package com.odenzo.xrpl.models.data.monetary

//


import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.{ Drops, FiatAmount, given }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Decoder.Result
import io.circe.literal.json
import io.circe.syntax.*
import io.circe.{ Encoder, Json }
import scodec.bits.{ BitVector, hex }

import java.util.UUID

class FiatValueOpsTest extends munit.FunSuite {
  private val log = LoggerFactory.getLogger

  val ops: FiatValue.fiatValueOps.type = FiatValue.fiatValueOps
  val fiat111                          = FiatValue("1111.11")

  /**
    * This is broken because exponent of 84 is out of range ANd need to deal
    * with special case of zero
    */
  test("FiatValue scodec - general") {
    val src     = FiatValue("120.230")
    val haha    = ops.packBigDecimal(src)
    val numeric = ops.toFiatValue.tupled(haha)
    val binary  = ops.packedToBinary.tupled(haha)
    log.info(s"\nNumeric: $src -> \n$haha -> \n$numeric")
    log.info(s"Binary Hex: ${binary.toHex}")
    log.info(s"Binary Bin: ${binary.toBin}")
  }

  test("FiatValue scodec - zero") {
    val src               = FiatValue("0.0")
    val haha              = ops.packBigDecimal(src)
    val numeric           = ops.toFiatValue.tupled(haha)
    val binary: BitVector = ops.packedToBinary.tupled(haha).padLeft(64).set(0)
    log.info(s"Binary Size: ${binary.size}")
    log.info(s"Binary Zero: ${binary.toHex}\nBinary: ${binary.toBin}")
    val expected          = FiatValue.fiatValueOps.zero
    log.info(s"Expected:  ${expected.size} ${expected.toHex}")
    assertEquals(binary.toBin, expected.toBin, "ZERO binary not same")
  }

}
