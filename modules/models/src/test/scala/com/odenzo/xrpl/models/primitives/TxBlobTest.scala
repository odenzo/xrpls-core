package com.odenzo.xrpl.models.primitives

import com.odenzo.xrpl.models.api.commands.Submit
import com.odenzo.xrpl.models.data.models.atoms.TxBlob
import com.tersesystems.blindsight.LoggerFactory
import io.circe.JsonObject
import io.circe.syntax.given
import spire.math.{ ULong, UShort }

class TxBlobTest extends munit.FunSuite {
  private val log = LoggerFactory.getLogger

  val validTxnBlobHex =
    "1200002280000000240000000361400000014B1A92C068400000000000022B73210330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD02074473045022100F847D12D1A37D91BCC7A394EA74C20D5AB726D4BCDF2843C33E0FBCFECC49C6602205B5964AA95163421291C4BDA37F4AD44E4B24DC439A0BE23E3141D0C49CD94CD8114B5F762798A53D543A014CAF8B297CFF8F2F937E883148B5F9BB7A5224D55C65E3B4BB426FD383EC43567"
  test("Circe Encoder") {
    val blob = TxBlob(validTxnBlobHex)

    log.info("Encoding")
    val json = blob.asJson
    log.info(s"Encoded: ${json.spaces4}")

    val rq = Submit.Rq(blob, true)
    log.info(s"Encoded SubmitRq: ${rq.asJson.spaces4}")
  }

}
