package com.odenzo.xrpl.signing.core

import cats.effect.IO
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import com.odenzo.xrpl.signing.testkit.CommandRqRsTestDataIOSpec
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.EncoderOps
// 026E1EF2D320E25D810A608272E0F76F25F1BD318FAFECA8C8F70AC996B3A4F596
/** Tests using the pubic key to derive the Account Address */
class DeriveAccountAddressTest
    extends CommandRqRsTestDataIOSpec[WalletPropose.Rq, WalletPropose.Rs]("WalletProposeRqRs.json") {

  import AccountAddress.given
  private val log = LoggerFactory.getLogger // Extension method

  def check(rs: WalletPropose.Rs)(using loc: munit.Location): Unit = {
    test(s"${rs.accountId.asBits.toHex} - ${rs.keyType}") {
      log.info(s"Inbound WalletPropseResult: ${rs.asJson.spaces4}")
      val publicKey: XrpPublicKey             = rs.publicKey
      println(s"Public Key: ${pprint.apply(publicKey)}")
      val publicKeyBytes                      = publicKey.asRawKey
      println(s"Raw Public Key: ${publicKeyBytes.toHex}")
      log.info(s"Expecting AccountAddress: ${rs.accountId.asJson.spaces4}")
      val computedAddress: IO[AccountAddress] = DeriveAccountAddress.xrpPublicKey2address(publicKey)
      assertIO(computedAddress, rs.accountId) // This is really AccountAddress (prefix and checksummed)

    }
  }

  testDataResource
    .use { rqrsl =>
      rqrsl.foreach { v => check(v._2) }
      IO.unit
    }.unsafeRunSync()
}
