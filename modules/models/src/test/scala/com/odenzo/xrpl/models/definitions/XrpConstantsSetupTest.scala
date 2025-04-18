package com.odenzo.xrpl.models.definitions

import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.models.internal.definitions.XrpConstantsSetup
import com.tersesystems.blindsight.LoggerFactory

class XrpConstantsSetupTest extends munit.FunSuite with BlindsightLogging {

  private val log = LoggerFactory.getLogger

  import io.circe.syntax.*

  test("Decode") {

    val res = XrpConstantsSetup.loadFromResource()
    log.debug("Constants Raw: " + res.asJson.spaces4)
    
    

  }
}
