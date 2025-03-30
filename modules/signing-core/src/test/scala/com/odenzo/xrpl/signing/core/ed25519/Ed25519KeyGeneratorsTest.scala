package com.odenzo.xrpl.signing.core.ed25519

import cats.effect.IO
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.keys.XrpKeyPair
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
class Ed25519KeyGeneratorsTest
    extends CommandRqRsTestDataIOSpec[WalletPropose.Rq, WalletPropose.Rs]("WalletProposeRqRs.json") {

  private val log = LoggerFactory.getLogger

  import com.odenzo.xrpl.models.data.atoms.AccountAddress
  import com.odenzo.xrpl.models.data.atoms.AccountAddress.given
  def check(rs: WalletPropose.Rs)(using loc: munit.Location): Unit = {
    test(s"${rs.accountId.asBits.toHex} - ${rs.keyType}") {
      import com.odenzo.xrpl.models.data.keys.XrpPublicKey.*
      log.debug(s"WalletRs: ${rs.asJson}")
      val keys: XrpKeyPair = Ed25519KeyGenerators.createXrpKeyPair(rs.masterSeed)
      println(s"XrpKeyPair Public Key ${keys.publicKey.bv.toHex}")

      val publicKey: ByteVector = keys.publicKey.bv
      println(s"XrpKeyPair Public Key ${keys.publicKey.bv.toHex}")
      assertEquals(publicKey.toHex(Alphabets.HexUppercase), rs.publicKeyHex, "Incorrect Public Key")

      for {
        accountAddr <- DeriveAccountAddress.xrpPublicKey2address(keys.publicKey)
        _            = assertEquals(accountAddr, rs.accountId, "AccountAddress Mismatch")
      } yield ()
    }
  }
  // I guess this can be a fixture accessed by check.
  testDataResource
    .use { (wallets: List[(WalletPropose.Rq, WalletPropose.Rs)]) =>
      wallets.filter(_._2.keyType.isEd25519).foreach(record => check(record._2)) // Named Tuples?
      IO.unit
    }.unsafeRunSync()
}

//  test("Wallet Shuffle") {
//    val account_id                                            = "r49pwNZibgeK83BeEuHYFKBpJE5Tt4USsQ"
//    val key_type                                              = "secp256k1"
//    val master_key                                            = "TILE TAKE WELD CASK NEWT TIRE WIND SOFA SHED HELL TOOK FAR"
//    val master_seed                                           = "ssDtFWc75geBLkzYcSYJ3nFbpRkaX"
//    val master_seed_hex                                       = hex"25DC4E4B6933FCFBD93F1CB2E6E3BCEB"
//    val public_key                                            = "aBPHrChJfFe7MtwyPtpf82CsseoW2X22M8dS4eAjWdrWGBX48gk5"
//    val public_key_hex                                        = "02ADBA6E42BCC1CEF0DA5CF2AC82A374C72ED7A78527976225D8AF49B82137934B"
//    log.info(s"Source Seed: ${master_seed_hex}")
//    log.info(s"Master Seed Prefix: ${TypePrefix.SeedValue.bv}")
//    val firstMasterSeed                                       = XrpSeed.fromBytesUnsafe(master_seed_hex)
//    log.info(s"FirstSeed RAW: ${firstMasterSeed.asRawSeed}")
//    log.info(s"FirstSeed : ${firstMasterSeed.bv}")
//    val accountKeys: XrpKeyPair[ByteVector, AccountPublicKey] = Generators.generateAllFromMasterSeed(firstMasterSeed)
//    val accountPublic: AccountPublicKey                       = accountKeys.publicKey
//    val computedRawPublicKey: ByteVector                      = accountPublic.asRawKey
//
//  }

// test("About Ripple") {
//    val masterSeed       = "F5933E5F60ED0F7A940E995B26F9191E"
//    val accountD         = "35778234256876764691539509132472247051158811702107230030471922537290092532408"
//    val accountPublicKey = "03AC6788F14F95D87AFF0236A3F671DBF774F24B9E9E94C2B188E9E82DD2F36C21"
//
//    val rippleD       = new BigInteger(accountD)
//    val rippleDHex    = ByteUtils.bytes2hex(rippleD.toByteArray)
//    logger.info(s"RippleD Hex:\n $rippleDHex")
//    val rippleKeyPair = Secp256K1CryptoBC.dToKeyPair(rippleD)
//    logger.info("Ripple KeyPair: " + rippleKeyPair.getPrivate)
//    val pub           = Secp256K1CryptoBC.compressPublicKey(rippleKeyPair.getPublic)
//    bytes2hex(pub) shouldEqual accountPublicKey
//  }
