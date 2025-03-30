package com.odenzo.xrpl.models.data.monetary

import com.tersesystems.blindsight.LoggerFactory

class XRPTest extends munit.FunSuite {

  private val log = LoggerFactory.getLogger

  test("Precision") {

    val big = CurrencyAmount.xrp(1000)
    log.info(s"Big     : $big")
  }

  test("MAX Precision") {
    val big = CurrencyAmount.xrp(Long.MaxValue)
    log.info(s"Big     : $big")
  }

}
