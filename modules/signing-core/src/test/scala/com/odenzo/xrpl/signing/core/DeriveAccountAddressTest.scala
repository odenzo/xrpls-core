package com.odenzo.xrpl.signing.core

import cats.effect.IO
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.keys.{ WalletProposeResult, XrpPublicKey }
import com.odenzo.xrpl.signing.testkit.WalletTestIOSpec
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.EncoderOps
// 026E1EF2D320E25D810A608272E0F76F25F1BD318FAFECA8C8F70AC996B3A4F596
/** Tests using the pubic key to derive the Account Address */
class DeriveAccountAddressTest extends WalletTestIOSpec {
  import AccountAddress.given
  import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.given
  private val log                                                           = LoggerFactory.getLogger // Extension method
  def check(walletRs: WalletProposeResult)(using loc: munit.Location): Unit = {
    test(s"${walletRs.account_id.asBits.toHex} - ${walletRs.key_type}") {
      log.info(s"Inbound WalletPropseResult: ${walletRs.asJson.spaces4}")
      val publicKey: XrpPublicKey             = walletRs.public_key
      println(s"Public Key: ${pprint.apply(publicKey)}")
      val publicKeyBytes                      = publicKey.asRawKey
      println(s"Raw Public Key: ${publicKeyBytes.toHex}")
      log.info(s"Expecting AccountAddress: ${walletRs.account_id.asJson.spaces4}")
      val computedAddress: IO[AccountAddress] = DeriveAccountAddress.xrpPublicKey2address(publicKey)
      assertIO(computedAddress, walletRs.account_id) // This is really AccountAddress (prefix and checksummed)

    }
  }

  walletDataResource
    .use { (wallets: List[WalletProposeResult]) =>
      wallets.foreach { (rs: WalletProposeResult) => check(rs) }
      IO.unit
    }.unsafeRunSync()
}
