package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.tersesystems.blindsight.LoggerFactory

class LedgerClosedTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("ledger_closed") {
    given engine: XrplEngine = engineFixture()
    val T                    = LedgerClosed
    val rq                   = LedgerClosed.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
