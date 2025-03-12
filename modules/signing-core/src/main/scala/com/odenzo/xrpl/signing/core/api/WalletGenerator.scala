package com.odenzo.xrpl.signing.core.api

//package com.odenzo.xrpl.signing.core.api
//
//
//import java.security.SecureRandom
//import cats.*
//import cats.data.*
//import cats.implicits.*
//import com.odenzo.xrpl.common.keys.KeyType
//import com.odenzo.xrpl.common.utils.ELogging
//import com.odenzo.xrpl.signing.core.ed25519.Ed25519KeyGenerators
//import com.odenzo.xrpl.signing.core.models.XrpSeed
//import com.odenzo.xrpl.signing.core.secp256k1.SecpKeyGenerators
//import org.bouncycastle.crypto.AsymmetricCipherKeyPair
//import scodec.bits.{BitVector, ByteVector}
//
//import java.nio.charset.StandardCharsets
//import scala.util.Random
//
///**
//  * This is the Scala API that accepts various "master keys"  and returns the master_seed_hex, account_address_hex, public_key_hex
// *
// * The idea is to receive arbitrary message and not expose our internal domain models at all.
//  */
//object WalletGenerator extends ELogging {
//
//  val secureRandom: SecureRandom = new SecureRandom()
//  val random: Random = new Random()
//
//  /**
//    * Generate a random wallet base on keyType = ed25519 or secp256k1.
//    */
//  def generateWallet(keytype: String): Either[Any, Any] = {
//      val passphrase: String = XrpSeed.randomPassphrase
//
//      keytype match {
//        case "ed25519"   => Ed25519KeyGenerators.createXrpKeyPair(seed)
//        case "secp256k1" => SecpKeyGenerators.createXrpKeyPair(seed)
//      }
//    }
//  }
//
//  /**
//    * Generates a wallet using Java SecureRandom, 16 random bytes. This is not
//    * packaged/wrapped
//    */
//  def generateSeed(): ByteVector = {
//    val ranBytes = new Array[Byte](16)
//    SecureRandom.getInstanceStrong.nextBytes(ranBytes)
//    ByteVector(ranBytes)
//  }
//
////
////  /**
////    * Please use the specific generateXXX whenever possible. Ideally generate
////    * from seed_hex. The
////    *
////    * @param someSeed
////    */
////  def generateSeedBySniffing(someSeed: String): Either[LocalOpsError, IndexedSeq[Byte]] = {
////    // Try and sniff the seed type and delegate, most specific to least with fallback
////    // Order is important, ie valid SecretKey cannot be any other thing
////    // Valid Hex cannot be valid B58Check. Also need to negative test all those routines.
////    // TODO: Seems  SeedB58 is not returning error on all failures (not checking prefix and checksum just chopping)
////    generateSeedFromSecretKey(someSeed)
////      .recoverWith { case e => generateSeedFromHex(someSeed) }
////      .recoverWith { case e => generateSeedFromSeedB58(someSeed) }
////      .recoverWith { case e => generateSeedFromPassword(someSeed) }
////}
//
//  def generateSeedFromSecretKey(wordsRFC1751: String): Either[LocalOpsError, IndexedSeq[Byte]] = {
//    for {
//      seedHex <- ConversionOps$.convertMasterKey2masterSeedHex(wordsRFC1751)
//      bytes   <- hex2bytes(seedHex)
//    } yield bytes.toIndexedSeq
//  }
//
//
//  /** This is XrpBase58, containing the prefix (s) and the checksum too. */
//  def generateSeedFromSeedB58(b58Check: String): Either[LocalOpsError, IndexedSeq[Byte]] = {
//    ConversionOps$.convertBase58Check2bytes(b58Check).map(_.toIndexedSeq)
//  }
//
///**
// * Creates a binary phrase and leftPads to 64 bits from the UTF-8 characters.
// * @param phrase
// * @return
// * @throws If all chars in the phrease are not valid UTF8
// */
//def generateSeedFromPassphrase(phrase: String): ByteVector =
//  ByteVector(phrase.getBytes(StandardCharsets.UTF_8)).padLeft(8)
//
//
//  /**
//    * Canonical generator given the "random" seed
//    *
//    * @param bytes
//    *   This requires exactly 16 bytes
//    */
//  def generateEdKeys(seed: ByteVector): Either[LocalOpsError, WalletProposeResult] = {
//
//    val kp: AsymmetricCipherKeyPair = ED25519CryptoBC.generateKeyPairFromBytes(bytes.toArray)
//    for {
//      pubHex    <- ED25519CryptoBC.publicKey2Hex(kp.getPublic)
//      pubB58    <- ConversionOps$.convertPubKeyHexToB58Check(Hex(pubHex))
//      pubBin    <- ByteUtils.hex2bytes(pubHex)
//      seedHex    = bytes2hex(bytes)
//      seedB58   <- ConversionOps$.convertSeedHexToB58Check(Hex(seedHex))
//      addr      <- ConversionOps$.accountpubkey2address(pubBin)
//      masterKey <- RFC1751Keys.bytesToEnglish(bytes.toArray)
//    } yield WalletProposeResult(
//      account_id      = addr.v,
//      key_type        = ED25519,
//      master_key      = masterKey,
//      master_seed     = seedB58.v,
//      master_seed_hex = seedHex,
//      public_key      = pubB58.v,
//      public_key_hex  = pubHex,
//    )
//  }
//
//  def generateSecpKeys(bytes: IndexedSeq[Byte]): Either[LocalOpsError, WalletProposeResult] = {
//
//    for {
//      kp     <- AccountFamily.rebuildAccountKeyPairFromSeed(bytes)
//      pubBin  = Secp256K1CryptoBC.compressPublicKey(kp.getPublic)
//      pubHex  = ByteUtils.bytes2hex(pubBin)
//      pubB58 <- ConversionOps$.convertPubKeyHexToB58Check(Hex(pubHex))
//
//      seedHex    = bytes2hex(bytes)
//      seedB58   <- ConversionOps$.convertSeedHexToB58Check(Hex(seedHex))
//      addr      <- ConversionOps$.accountpubkey2address(pubBin)
//      masterKey <- RFC1751Keys.bytesToEnglish(bytes.toArray)
//    } yield WalletProposeResult(
//      account_id      = addr.v,
//      key_type        = SECP256K1,
//      master_key      = masterKey,
//      master_seed     = seedB58.v,
//      master_seed_hex = seedHex,
//      public_key      = pubB58.v,
//      public_key_hex  = pubHex,
//    )
//  }
//
//}
