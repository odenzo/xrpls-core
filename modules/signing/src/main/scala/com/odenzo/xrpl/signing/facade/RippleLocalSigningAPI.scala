package com.odenzo.xrpl.signing.facade



//package com.odenzo.ripple.signing
//
//import cats.*
//import cats.data.*
//import cats.implicits.*
//import com.odenzo.ripple.signing.impl.{ Sign, SignFor }
//import com.odenzo.ripple.signing.impl.crypto.ConversionOps
//import com.odenzo.ripple.signing.models.SigningKey
//
//import io.circe.{ Json, ParsingFailure }
//import io.circe.syntax.*
//import scodec.bits.ByteVector
//
///**
//  * This is the simple API to use the Ripple Local Operations. See
//  * RippleLocalOps for a superset of this API that may be handy for existing
//  * code bases. Convertors from different Strings to ByteVector, as well as
//  * TxJson to ByteVector so just about any external models can be used.
//  */
//
//object RippleLocalSigningAPI {
//
//  object adaptors {
//    def parseJson(str: String): Either[ParsingFailure, Json] = io.circe.parser.parse(str)
//
//    /** Convert Hex string to ByteVector */
//    def hexToByteVector(hex: String): Either[String, ByteVector] =
//      ByteVector.fromHexDescriptive(hex)
//
//  }
//
//  /**
//    * This is the recommended programmatic API for Local Signing. The TxBlob
//    * response is the only item needed for subsequent `submit` to XRPL server.
//    * This is for signle signing.
//    * @param tx_json
//    *   TxJson fully formed (auto-filled) to submit
//    * @param signingKey
//    *   Prepackaged signing key.
//    *
//    * @return
//    *   TxBlob in Hex form suitable for submitting to Rippled XRPL
//    */
//  def signTxn(tx_json: Json, signingKey: ByteVector) = {
//    for {
//      sig      <- Sign.signToTxnSignature(tx_json, signingKey)
//      txblob   <- Sign.createSignedTxBlob(tx_json, sig)
//      txblobHex = ByteUtils.bytes2hex(txblob)
//    } yield txblobHex
//  }
//
//  /** This created a Signature to be added to a multisigned Txn */
//  def signToTxnSignature(tx_json: Json, signingKey: SigningKey): Either[LocalOpsError, String] = {
//    Sign.signToTxnSignature(tx_json, signingKey).map(_.hex)
//  }
//
//  /**
//    * Multisignes a tx_json object with one key, adds Signer to existing Signers
//    * or creates Signers with exactly Signer
//    * @param tx_json
//    * @param key
//    * @param signingAcct
//    * @return
//    *   An updated tx_json with addition Signer in the Signers field (correctly
//    *   sorted)
//    */
//  def multisign(tx_json: Json, key: SigningKey, signingAcct: String): Either[Throwable, Json] = {
//    SignFor.signFor(tx_json, key, signingAcct)
//  }
//
//  /**
//    * If you signFor the base tx_json N times you get N signFor responses each
//    * with one Signer. This merges those all together into something that can be
//    * submitted. Its the full response with embedded tx_json updates
//    * @param rs
//    *   of tx_json objects with Signers fields and Signer objects to merge
//    */
//  def multisignMerge(rs: List[Json]): Either[LocalOpsError, Json] = {
//    SignFor.mergeMultipleTxJsonResponses(rs)
//  }
//
//  /**
//    * Once all the signatures on in a tx_json structure this is used to
//    * calcaulre the tx_blob for submittsion No Additional signing is done.
//    */
//  def multisignedToTxBlob(tx_json: Json): Either[LOpException, String] = {
//    SignFor.generateTxBlob(tx_json)
//
//  }
//
//  /**
//    * This is recommended API for verifying a Signature. I have yet to run
//    * across a client side use-case for this.
//    *
//    * Takes a signed tx_json object and verified the TxnSignature usign
//    * SigningPubKey .
//    *
//    * This is updated to deal with multisigned and single signed.
//    *
//    * @param tx_json
//    *
//    * @return
//    *   true if verified correctly
//    */
//  def verify(tx_json: Json): Either[LocalOpsError, Boolean] = {
//    Verification.verifySignedResponse(tx_json)
//  }
//
//  /**
//    * Generates two sets of keys, but doesn't activate them in any way.
//    *
//    * @return
//    *   Master and Regular KeyPair based on random seed.
//    */
//  def generateAccountKeys(key_type: String): Either[LocalOpsError, Json] = {
//
//    for {
//      keyType <- KeyType.fromText(key_type).leftMap(e => LocalOpsError(e.error_message.getOrElse("Bad KeyType")))
//      master  <- WalletGenerator.generateWallet(keyType)
//    } yield (master.asJson)
//  }
//
//  /**
//    * Wrapws keypair in SigningKey for optimzed usage. Typically client cache's
//    * this.
//    *
//    * @param master_seed_hex
//    *   The master seed of an keypair, either master or regular keypair.
//    * @param key_type
//    *   ED25519 or SECP2561K1
//    *
//    * @return
//    *   An opaque structure that should not be relied on for anyway. Clients
//    *   should persist keys using the master_seed in an encrypted vault.
//    */
//  def packSigningKey(master_seed_hex: String, key_type: String): Either[LocalOpsError, SigningKey] = {
//    KeyType
//      .fromText(key_type)
//      .leftMap(e => LocalOpsError(s"Bad Input for key_type ${key_type} + ${e.error_message}"))
//      .flatMap(Sign.preCalcKeys(master_seed_hex, _))
//  }
//
//  /**
//    * Wrapws keypair in SigningKey for optimzed usage. Typically client cache's
//    * this.
//    *
//    * @param master_seed
//    *   The master seed of an keypair, either master or regular keypair.
//    * @param key_type
//    *   ED25519 or SECP2561K1
//    *
//    * @return
//    *   An opaque structure that should not be relied on for anyway. Clients
//    *   should persist keys using the master_seed in an encrypted vault.
//    */
//  def packSigningKeyFromB58(master_seed: String, key_type: String): Either[LocalOpsError, SigningKey] = {
//    ConversionOps.convertBase58Check2hex(master_seed).flatMap(packSigningKey(_, key_type))
//  }
//
//  /** Pack a key into internal format. Parameters per WalletProposeRs */
//  def packSigningKeyFromRFC1751(master_key: String, key_type: String): Either[LocalOpsError, SigningKey] = {
//    ConversionOps.convertMasterKey2masterSeedHex(master_key).flatMap(packSigningKey(_, key_type))
//  }
//
//  def precomputeSigningKey(master_seed: String, key_type: String): Either[LocalOpsError, SigningKey] = {
//    packSigningKeyFromB58(master_seed, key_type)
//  }
//
//}
