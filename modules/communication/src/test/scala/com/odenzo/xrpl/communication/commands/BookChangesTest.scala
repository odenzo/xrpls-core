package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.xrp
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class BookChangesTest extends LocalCommsTest(TestScenarios.mode) with BlindsightLogging {

  private val log = LoggerFactory.getLogger

  test("Valid Call") {
    given engine: XrplEngine = engineFixture()

    val T  = BookChanges
    val rq = T.Rq()
    for {
      response <- engine.send[T.Rq, T.Rs](rq)
      _         = log.info(s"Response ModelJSON: ${response.rs.asJson.spaces4}")
    } yield response

  }

  // TODO: Needs a Scenario Test
}
