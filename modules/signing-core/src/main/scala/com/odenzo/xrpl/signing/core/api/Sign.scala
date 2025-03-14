//package com.odenzo.xrpl.signing.core.api
//
////import com.odenzo.ripple.signing.impl.crypto.AccountFamily
////import com.odenzo.ripple.signing.impl.crypto.core.{ED25519CryptoBC, KeyType, TxnSignature}
////import com.odenzo.ripple.signing.models.{SigningKey, SigningKeyEd25519}
//import com.odenzo.xrpl.common.utils.MyLogging
//import com.odenzo.xrpl.common.xrpconstants.TxHashPrefix
//import io.circe.Json
//import scodec.bits.ByteVector
//
/////** This takes a message and signs it. Returning the TxBlob */
//object SignController extends MyLogging {
//
//  /** Add AccountAddress in this for future Multisigning */
//  def preCalcKeys(seed: ByteVector, keyType: KeyType) = {
//    keyType match {
//      case KeyType.ed25519   =>
//        for {
//          keys <- ED25519CryptoBC.generateKeyPairFromBytes(seed)
//          spub <- ED25519CryptoBC.publicKey2Hex(keys.getPublic)
//        } yield SigningKeyEd25519(keys, spub)
//      case KeyType.secp256k1 =>
//        for {
//          keys <- AccountFamily.rebuildAccountKeyPairFromSeedHex(seedhex)
//          spub  = Secp256K1CryptoBC.publicKey2hex(keys.getPublic)
//        } yield SigningKeySecp256(keys, spub)
//    }
//  }
//
//  }
//
//
//
////
//
////
////}
