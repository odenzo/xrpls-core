package com.odenzo.xrpl.models.data.binary

import com.odenzo.xrpl.models.data.flags.{ Flags, PaymentFlag }
import com.tersesystems.blindsight.*
import munit.FunSuite

class FlagTest extends FunSuite {

  private val log = LoggerFactory.getLogger

  test("Creation from Long") {
    val example = Flags.fromLong(2147483648L)
    log.info(s"ExampleBits: ${example.bits}")
    val got     = Flags.fromLong(483648L)
    log.info(s"GotBits: ${got.bits}")
  }

  test("PaymentTx Flags") {
    PaymentFlag.values.foreach { flag =>
      val bv = flag.v.toBin
    }
  }
}
