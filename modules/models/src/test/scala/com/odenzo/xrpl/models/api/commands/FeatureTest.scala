package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Submit
import com.odenzo.xrpl.models.data.models.atoms.TxBlob
import com.tersesystems.blindsight.LoggerFactory
import io.circe.JsonObject
import io.circe.literal.json
import io.circe.syntax.given
import spire.math.{ ULong, UShort }
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import io.circe.optics.JsonPath.root
import io.circe.pointer.PointerFailure
import io.circe.pointer.literal.pointer
import io.circe.{ Codec, Decoder, Encoder, Json }
class FeatureTest extends munit.CatsEffectSuite {
  private val log = LoggerFactory.getLogger

  import Feature.{ *, given }

  val singleResponse = json""" {
                                 "result" : {
                                     "96FD2F293A519AE1DB6F8BED23E4AD9119342DA7CB6BAFD00953D16C54205D8B" : {
                                         "enabled" : false,
                                         "name" : "PriceOracle",
                                         "supported" : true,
                                         "vetoed" : false
                                     },
                                     "status" : "success"
                                 }
                             }"""

  test("Extract Status") {
    val rs = singleResponse
    IO.fromOption(root.result.status.as[String].getOption(rs))(IllegalStateException(s"No Status Found"))

  }

}
