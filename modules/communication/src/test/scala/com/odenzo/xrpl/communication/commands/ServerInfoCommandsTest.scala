package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.ledgerinfo.LedgerCurrent
import com.odenzo.xrpl.models.api.commands.serverinfo.{ Fee, ServerDefinitions }
import com.tersesystems.blindsight.LoggerFactory

class ServerInfoCommandsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("Fee") {
    given engine: XrplEngine = engineFixture()
    val T                    = Fee
    val rq                   = Fee.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)
    response

  }

  test("ledger_current") {
    given engine: XrplEngine = engineFixture()
    val T                    = LedgerCurrent
    val rq                   = LedgerCurrent.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)
    response
  }

  test("server_definitions") {
    given engine: XrplEngine = engineFixture()
    val T                    = ServerDefinitions
    val rq                   = ServerDefinitions.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
