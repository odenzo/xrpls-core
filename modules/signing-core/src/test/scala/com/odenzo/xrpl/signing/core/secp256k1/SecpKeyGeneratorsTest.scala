package com.odenzo.xrpl.signing.core.secp256k1

import cats.effect.IO
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, WalletProposeResult, XrpKeyPair, XrpSeed }
import com.odenzo.xrpl.signing.core.ed25519.Ed25519KeyGenerators
import com.odenzo.xrpl.signing.core.DeriveAccountAddress
import com.odenzo.xrpl.signing.testkit.WalletTestIOSpec
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.EncoderOps
import scodec.bits.Bases.Alphabets
import scodec.bits.ByteVector

/**
  * Check to see the Secp Wallet Generator is accurate for MasterSeedHex
  *   - MasterSeed and MasterSeedHex isomorphism tested elsewhere.
  *   - Passphrase to MasterSeedHex tested elsewhere.
  */
class SecpKeyGeneratorsTest extends WalletTestIOSpec {
  import com.odenzo.xrpl.models.data.models.atoms.AccountAddress.given
  private val log                                                           = LoggerFactory.getLogger // Extension method
  import XrpSeed.given
  def check(walletRs: WalletProposeResult)(using loc: munit.Location): Unit = {
    test(s"${walletRs.account_id.asBits.toHex} - ${walletRs.key_type}") {
      log.info(s"Full Wallet: ${walletRs.asJson.spaces4}")
      log.info(s"Wallet MasterSeedHex: ${walletRs.master_seed_hex}")
      val seed: XrpSeed    = XrpSeed.fromMasterSeedBase58(walletRs.master_seed)
      println(s"MasterSeedHex: ${seed.bits.toHex}")
      val keys: XrpKeyPair = walletRs.key_type match
        case KeyType.secp256k1 => SecpKeyGenerators.createXrpKeyPair(seed)
        case KeyType.ed25519   => fail("Invalid KeyType - Not Secp")

      import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.*
      val publicKey: ByteVector = keys.publicKey.bv
      assertEquals(publicKey.toHex(Alphabets.HexUppercase), walletRs.public_key_hex, "Incorrect Public Key")
      for {
        accountAddr <- DeriveAccountAddress.xrpPublicKey2address(keys.publicKey)
        _            = assertEquals(accountAddr, walletRs.account_id, "AccountAddress Mismatch")
      } yield ()

    }
  }

  walletDataResource
    .use { (wallets: List[WalletProposeResult]) =>
      wallets.filter(_.isSecp256k1).foreach { (rs: WalletProposeResult) => check(rs) }
      IO.unit
    }.unsafeRunSync()
}
