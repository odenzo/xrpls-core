package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.tersesystems.blindsight.LoggerFactory

class FeeTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("Fee") {
    given engine: XrplEngine = engineFixture()
    val T                    = Fee
    val rq                   = T.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq)
    response

  }

}
