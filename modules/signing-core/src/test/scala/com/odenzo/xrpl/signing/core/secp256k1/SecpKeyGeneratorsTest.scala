package com.odenzo.xrpl.signing.core.secp256k1

import cats.effect.IO
import com.odenzo.xrpl.models.api.commands.admin.keygen.WalletPropose
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, WalletProposeResult, XrpKeyPair, XrpSeed }
import com.odenzo.xrpl.signing.core.ed25519.Ed25519KeyGenerators
import com.odenzo.xrpl.signing.core.DeriveAccountAddress
import com.odenzo.xrpl.signing.testkit.CommandRqRsTestDataIOSpec
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.EncoderOps
import scodec.bits.Bases.Alphabets
import scodec.bits.ByteVector

/**
  * Check to see the Secp Wallet Generator is accurate for MasterSeedHex
  *   - MasterSeed and MasterSeedHex isomorphism tested elsewhere.
  *   - Passphrase to MasterSeedHex tested elsewhere.
  */
class SecpKeyGeneratorsTest extends CommandRqRsTestDataIOSpec[WalletPropose.Rq, WalletPropose.Rs]("WalletProposeRqRs.json") {
  import com.odenzo.xrpl.models.data.models.atoms.AccountAddress.given
  private val log                                                        = LoggerFactory.getLogger // Extension method
  import XrpSeed.given
  def check(walletRs: WalletPropose.Rs)(using loc: munit.Location): Unit = {
    test(s"${walletRs.accountId.asBits.toHex} - ${walletRs.keyType}") {
      log.info(s"Full Wallet: ${walletRs.asJson.spaces4}")
      log.info(s"Wallet MasterSeedHex: ${walletRs.masterSeedHex}")
      val seed: XrpSeed    = walletRs.masterSeed
      println(s"MasterSeedHex: ${seed.bits.toHex}")
      val keys: XrpKeyPair = walletRs.keyType match
        case KeyType.secp256k1 => SecpKeyGenerators.createXrpKeyPair(seed)
        case KeyType.ed25519   => fail("Invalid KeyType - Not Secp")

      import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.*
      val publicKey: ByteVector = keys.publicKey.bv
      assertEquals(publicKey.toHex(Alphabets.HexUppercase), walletRs.publicKeyHex, "Incorrect Public Key")
      for {
        accountAddr <- DeriveAccountAddress.xrpPublicKey2address(keys.publicKey)
        _            = assertEquals(accountAddr, walletRs.accountId, "AccountAddress Mismatch")
      } yield ()

    }
  }

  testDataResource
    .use { rqrsl =>
      rqrsl.filter(v => v._2.keyType.isSecp256k1).foreach { rqrs => check(rqrs._2) }
      IO.unit
    }.unsafeRunSync()
}
