package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpSeed }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

class VersionTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("version") {
    given engine: XrplEngine = engineFixture()
    val rq: Version.Rq  = Version.Rq()
    log.info(rq.asJson.spaces4)
    val response             = engine.send[Version.Rq, Version.Rs](rq)
    response
  }

}
