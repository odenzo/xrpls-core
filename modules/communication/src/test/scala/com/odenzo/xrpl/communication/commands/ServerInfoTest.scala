package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpSeed }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

class ServerInfoTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("server_info") {
    given engine: XrplEngine = engineFixture()

    val rq: ServerInfo.Rq = ServerInfo.Rq(true)
    log.info(rq.asJson.spaces4)
    val response = engine.send[ServerInfo.Rq, ServerInfo.Rs](rq)
    response
  }

}
