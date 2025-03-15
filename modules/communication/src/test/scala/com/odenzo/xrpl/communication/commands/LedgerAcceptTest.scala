package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpSeed }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

class LedgerAcceptTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("ledger_accept") {
    given engine: XrplEngine = engineFixture()
    val rq: LedgerAccept.Rq  = LedgerAccept.Rq()
    log.info(rq.asJson.spaces4)
    val response             = engine.send[LedgerAccept.Rq, LedgerAccept.Rs](rq)
    response
  }

}
