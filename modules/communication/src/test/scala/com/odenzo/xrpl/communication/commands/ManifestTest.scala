package com.odenzo.xrpl.communication.commands

import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import com.tersesystems.blindsight.LoggerFactory

class ManifestTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("Simple Test") {
    given engine: XrplEngine    = engineFixture()
    val T                       = Manifest
    val publicKey: XrpPublicKey = ???
    val rq                      = T.Rq(publicKey)
    val response                = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
