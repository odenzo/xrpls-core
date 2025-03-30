package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.tersesystems.blindsight.LoggerFactory

class LedgerTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("ledger") {
    given engine: XrplEngine = engineFixture()

    val T        = Ledger
    val rq       = Ledger.Rq(
      ledgerIndex  = LedgerHandle.validated,
      transactions = true,
      accounts     = true,
      expand       = true,
    )
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
