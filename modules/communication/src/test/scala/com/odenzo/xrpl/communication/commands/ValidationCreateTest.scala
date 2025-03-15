package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpSeed }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

class ValidationCreateTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("validation_create") {
    val T: ValidationCreate.type = ValidationCreate
    given engine: XrplEngine     = engineFixture()
    val response                 = engine.send[T.Rq, T.Rs](T.Rq(None))
    response
  }

}
