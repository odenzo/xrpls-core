package com.odenzo.xrpl.signing.testkit

import cats.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.rpc.RpcPayloadOps
import io.circe.{ Codec, Decoder, Encoder, JsonObject }
import munit.CatsEffectSuite
import munit.catseffect.IOFixture

/**
  * This should handle all commands except the Signing and Submission commands.
  * It only operates in RPC mode for now
  */
trait CommandRqRsTestDataIOSpec[RQ: Encoder.AsObject: Decoder, RS: Encoder.AsObject: Decoder](
    src: String
) extends CatsEffectSuite {

  case class RqRsPair(rq: JsonObject, rs: JsonObject) derives Codec.AsObject

  def extractRqRs(v: RqRsPair): IO[(RQ, RS)] = {
    val rq = v.rq.toJson
    val rs = v.rs.toJson
    for {
      command   <- RpcPayloadOps.extractCommand(rq)
      request   <- RpcPayloadOps.extractRequest(rq)
      commandRq <- IO.fromEither(request.as[RQ])
      response  <- RpcPayloadOps.extractResult(rs)
      commandRs <- IO.fromEither(response.as[RS])
    } yield (commandRq, commandRs)

  }

  def loadResourceAsRequestResponseJson: IO[List[(RQ, RS)]] =
    for {
      json    <- IO.blocking(TestUtils.loadResourceNamedAsJson(src))
      pairs   <- IO.fromEither(json.as[List[RqRsPair]].leftMap(err => Exception(s"Error Decoding JSON from $src", err)))
      decoded <- pairs.traverse(rqrs => extractRqRs(rqrs))
    } yield decoded

  val testData: IO[List[(RQ, RS)]] = loadResourceAsRequestResponseJson

  /** A list of Signing Rq and Rs  generated from stand-alone XRPL server */
  val testDataResource: Resource[IO, List[(RQ, RS)]] = Resource.eval(testData)

  val testDataFixture: IOFixture[List[(RQ, RS)]] = ResourceSuiteLocalFixture(
    "request-response-data",
    resource = testDataResource,
  )

  override def munitFixtures = List(testDataFixture)

}
