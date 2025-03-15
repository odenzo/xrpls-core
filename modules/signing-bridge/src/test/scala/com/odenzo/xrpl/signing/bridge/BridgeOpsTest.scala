package com.odenzo.xrpl.signing.bridge

import cats.effect.{ IO, Resource }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.keys.{ WalletProposeResult, XrpPublicKey }
import com.odenzo.xrpl.signing.core.DeriveAccountAddress
import com.odenzo.xrpl.signing.testkit.CommandRqRsTestDataIOSpec
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.EncoderOps
// 026E1EF2D320E25D810A608272E0F76F25F1BD318FAFECA8C8F70AC996B3A4F596

/** Tests using the pubic key to derive the Account Address */
class BridgeOpsTest extends CommandRqRsTestDataIOSpec[Sign.Rq, Sign.Rs]("SignRqRs.json") {
  import AccountAddress.given
  import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.given
  private val log = LoggerFactory.getLogger // Extension method

  val tdr: Resource[IO, List[(Sign.Rq, Sign.Rs)]] = testDataResource

  tdr
    .use { (rqrs: List[(Sign.Rq, Sign.Rs)]) =>
      rqrs.foreach { rqrs => check.tupled }
      IO.unit
    }.unsafeRunSync()

  def check(rq: Sign.Rq, rs: Sign.Rs)(using loc: munit.Location): Unit = {
    test(s"BridgeOps XXX for SignRq: ${rq.keyType}") {}

  }

}
