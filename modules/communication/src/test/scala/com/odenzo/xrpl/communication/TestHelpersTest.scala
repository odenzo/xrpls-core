package com.odenzo.xrpl.communication

import com.tersesystems.blindsight.LoggerFactory

class TestHelpersTest extends LocalCommsTest(TestScenarios.mode) {
  private val log = LoggerFactory.getLogger

  test("Test amendment enablement") {
    given engine: XrplEngine = engineFixture()
    TestHelpers.enableAllAmendments
  }

}
