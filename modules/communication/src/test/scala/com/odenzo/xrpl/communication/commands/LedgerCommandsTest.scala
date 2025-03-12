package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.ledgerinfo.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.tersesystems.blindsight.LoggerFactory

class LedgerCommandsTest extends LocalCommsTest(TestScenarios.mode) {

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

  test("ledger_closed") {
    given engine: XrplEngine = engineFixture()
    val T                    = LedgerClosed
    val rq                   = LedgerClosed.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)
    response
  }

  test("ledger_current") {
    val T = LedgerCurrent

    given engine: XrplEngine = engineFixture()
    val rq                   = LedgerCurrent.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)

    response

  }

  test("ledger_data") {
    val T = LedgerData

    given engine: XrplEngine = engineFixture()

    val rq       = T.Rq(LedgerHandle.validated, binary = false)
    val response = engine.send[T.Rq, T.Rs](rq)

    response

  }

  test("ledger_entry".fail) {
    val T                    = LedgerEntry
    given engine: XrplEngine = engineFixture()
    val rq                   = T.Rq(binary = false)
    val response             = engine.send[T.Rq, T.Rs](rq)
    response

  }
}
