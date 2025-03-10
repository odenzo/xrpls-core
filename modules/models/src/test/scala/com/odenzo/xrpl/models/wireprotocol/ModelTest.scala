package com.odenzo.xrpl.models.wireprotocol

import com.odenzo.xrpl.common.utils.MyLogging
import com.tersesystems.blindsight.{ Logger, LoggerFactory }
import io.circe.{ Decoder, Encoder, Json }
import io.circe.Decoder.Result
import munit.*
import munit.Assertions.fail

/**
  * Slowly rebuilding a suite of unit tests for the models, which don't require
  * calling Ripple. Not sure this is really worth the effort but the AnyVal
  * classes fight with import optimization
  */
trait ModelTest extends MyLogging {

  protected val log: Logger = LoggerFactory.getLogger

  /**
    * Utility to parse a JSON string. Will fail test if cannot parse.
    * @param s
    * @return
    */
  def parse(s: String): Json = {
    io.circe.parser.parse(s) match {
      case Left(err)   => munit.Assertions.fail(s"Could not parse json $err from String:\n$s")
      case Right(json) => json
    }
  }

  /**
    * For the things that have both encoders and decoders (not rq encoder , rs
    * decoder)
    */
  def testRoundtrip[T](o: T, encoder: Encoder[T], decoder: Decoder[T]): Unit = {
    val json: Json = testEncoding(o, encoder)
    val o2: T      = testDecoding(json, decoder)
    munit.Assertions.assertEquals(o2, o)
  }

  def testDecoding[T](json: Json, decoder: Decoder[T]): T = {
    decoder.decodeJson(json) match {
      case Left(err) => fail(s"Could not decode json $err \n from the JSON:\n${json.spaces2}")
      case Right(ok) => log.debug(s"Decoded: $ok"); ok
    }
  }

  /**
    * Parses the input string and applies the decoding. Logging errors and
    * asserting success
    */
  def testDecoding[T](s: String, decoder: Decoder[T]): T = {
    testDecoding(parse(s), decoder)
  }

  /**
    * This tries to apply the encoder, which can never fail except exceptions
    * which we let go
    */
  def testEncoding[T](t: T, encoder: Encoder[T]): Json = {
    val res: Json = encoder.apply(t)
    log.debug(s"Encoding $t (${t.getClass} of ${encoder} Yields:\n${res.spaces2}")
    res
  }

  def logResult[A](a: Result[A], msg: String = "Circe Decoding"): Unit = {
    a match {
      case Left(err) => log.warn(s"$msg Error: $err")
      case Right(v)  => log.debug(s"$msg Parsing OK   : $v")
    }
  }
}
