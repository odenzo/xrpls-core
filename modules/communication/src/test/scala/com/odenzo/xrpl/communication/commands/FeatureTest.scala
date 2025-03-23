package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.communication.models.XrplEngineCommandResult
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.xrp
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*
import scala.concurrent.duration.*

/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class FeatureTest extends LocalCommsTest(TestScenarios.mode) with BlindsightLogging {

  private val log = LoggerFactory.getLogger

  test("Feature Codecs") {
    given engine: XrplEngine = engineFixture()

    val T                                                      = Feature
    val rq                                                     = T.Rq(None, None)
    val allAmendments: IO[XrplEngineCommandResult[Feature.Rs]] = for {
      response <- engine.send[T.Rq, T.Rs](rq)
      first     = response.rs.features.filter((h, a) => a.supported).toList.head
      turnOn    = T.Rq(first._1.some, false.some)
      singleRs <- engine.send[T.Rq, T.Rs](turnOn)

    } yield response
    allAmendments
  }

  // This is elaborated in TestHelpers and Scanario tested there.
}
