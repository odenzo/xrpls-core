package com.odenzo.xrpl.signing.bridge

import cats.effect.{ IO, Resource }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.signing.testkit.CommandRqRsTestDataIOSpec
import com.tersesystems.blindsight.{ Condition, LoggerFactory }

/**
  * Tests using the public key to derive the Account Address -TODO: Not
  * Implemented
  */
class BridgeOpsTest extends CommandRqRsTestDataIOSpec[Sign.Rq, Sign.Rs]("SignRqRs.json") {
  private val log = LoggerFactory.getLogger.withCondition(condition = Condition.never)

  val tdr: Resource[IO, List[(Sign.Rq, Sign.Rs)]] = testDataResource
  log.warn("Not Implemented")
  tdr
    .use { (rqrs: List[(Sign.Rq, Sign.Rs)]) =>
      rqrs.foreach { rqrs => check.tupled }
      IO.unit
    }.unsafeRunSync()

  def check(rq: Sign.Rq, rs: Sign.Rs)(using loc: munit.Location): Unit = {
    test(s"BridgeOps XXX for SignRq: ${rq.keyType}") {}

  }

}
