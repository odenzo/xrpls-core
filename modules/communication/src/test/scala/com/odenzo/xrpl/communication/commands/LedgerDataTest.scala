package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.tersesystems.blindsight.LoggerFactory

class LedgerDataTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("ledger_data") {
    val T = LedgerData

    given engine: XrplEngine = engineFixture()

    val rq       = T.Rq(LedgerHandle.validated, binary = false)
    val response = engine.send[T.Rq, T.Rs](rq)

    response

  }

}
