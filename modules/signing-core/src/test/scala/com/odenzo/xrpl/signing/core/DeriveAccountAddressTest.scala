package com.odenzo.xrpl.signing.core

import cats.effect.IO
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.keys.{ WalletProposeResult, XrpPublicKey }
import com.odenzo.xrpl.signing.testkit.WalletTestIOSpec
import com.tersesystems.blindsight.LoggerFactory

/** Tests using the pubic key to derive the Account Address */
class DeriveAccountAddressTest extends WalletTestIOSpec {
  import AccountAddress.given
  private val log                                                           = LoggerFactory.getLogger // Extension method
  def check(walletRs: WalletProposeResult)(using loc: munit.Location): Unit = {
    test(s"${walletRs.account_id.asBits.toHex} - ${walletRs.key_type}") {
      val publicKey: XrpPublicKey      = walletRs.public_key
      val computed: IO[AccountAddress] = DeriveAccountAddress.xrpPublicKey2address(publicKey)
      assertIO(computed, walletRs.account_id) // This is really AccountAddress (prefix and checksummed)

    }
  }

  walletDataResource
    .use { (wallets: List[WalletProposeResult]) =>
      wallets.foreach { (rs: WalletProposeResult) => check(rs) }
      IO.unit
    }.unsafeRunSync()
}
