package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.tersesystems.blindsight.LoggerFactory

class LedgerCurrentTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("ledger_current") {
    given engine: XrplEngine = engineFixture()
    val T                    = LedgerCurrent
    val rq                   = LedgerCurrent.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
