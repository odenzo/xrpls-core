package com.odenzo.xrp.bincodec

import com.odenzo.xrp.bincodec.controllers.EncoderController
import com.odenzo.xrpl.models.internal.definitions.XrpConstantsSetup
import com.odenzo.xrpl.models.scodecs.MetaData
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Json
import io.circe.literal.*

/**
  * Just to incrementally build a fake transaction will lots of fields in no
  * sane way. Then check the encoding, before going to do test specifc Codecs in
  * detail
  */
class DevTests extends munit.FunSuite {
  
  private val log = LoggerFactory.getLogger
  MetaData.touch()
  XrpConstantsSetup.touch()
  test("SimplePayment") {
    val json: Json  = json""" {
   
  "TransactionType": "Payment",
  "Account": "rPT1Sjq2YGrBMTttX4GZHjKu9dyfzbpAYe",
  "DeliverMin": "2000000",
  "Destination": "rUCzEr6jrEyMpjhs4wSdQdz4g8Y382NxfM"
  }     """
//
    val bitsAttempt = EncoderController.topLevelSerializingEncoder.encode(json)
    val bits        = bitsAttempt.require
    log.info(s"Result Bits: \n ${bits.toHexDumpColorized}")
  }
}
