package com.odenzo.xrpl.signing.bridge

import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.atoms.TxnSignature
import com.odenzo.xrpl.models.data.keys.{ KeyType, XrpKeyPair, XrpSeed }
import com.odenzo.xrpl.models.scodecs.XrpBinCodecAPI
import com.odenzo.xrpl.signing.core.ed25519.{ Ed25519KeyGenerators, Ed25519Signing }
import com.odenzo.xrpl.signing.core.passphases.{ PassphraseOps, RFC1751Keys }
import com.odenzo.xrpl.signing.core.secp256k1.{ SecpKeyGenerators, SecpSigning }
import scodec.bits.BitVector

/**
  * First cut is a stand-in for the normal Sign.Rq / Sign.Rs instead of sending
  * it to XRPL server.
  */
object BridgeOps {

  /**
    * @param rq
    *   Full SignRq (SingingPubKey doesn't need to be filled) All the auto-fill
    *   fields should be filled though.
    * @return
    *   Signing Rs so it can be submmitted
    */
  def createTxnSignature(rq: Sign.Rq): TxnSignature = {
    val txJson             = rq.txJson.toJson
    val encoded: BitVector = XrpBinCodecAPI.encode(txJson, true)
    TxnSignature(encoded.bytes)
  }

  /**
    * Get the Seed from eithe seed or passphrase in RFC1751 or plaintext form.
    * Ignoring Seed Hex?
    */
  def calculateXrpSeed(rq: Sign.Rq): XrpSeed = {
    rq.seed.getOrElse {
      rq.passphrase match
        case Some(phrase) => PassphraseOps.seedFromString(phrase)
        case None         => throw new IllegalArgumentException("No Passphrase or Seed in Sign Rq")
    }
  }

  /**
    * Confused with legacy, do I want to sign this with the PublicKey or Private
    * Key
    */
  def signTxnSignature(sig: TxnSignature, seed: XrpSeed, keyType: KeyType) = {
    // val signed: Unit = signTxnSignature(signature, rq.keyType, seed)
  }

//      withPubKey = tx_json.mapObject(_.add("SigningPubKey", key.signPubKey.asJson))
//      sig       <- Sign.signToTxnSignature(withPubKey, key).leftMap(err => ResponseError.kBadSecret)
//
//      txBlob <- Sign.createSignedTxBlob(withPubKey, sig).leftMap(err => ResponseError.kBadSecret)
//      blobhex = ByteUtils.bytes2hex(txBlob)
//      hash    = Sign.createResponseHashHex(txBlob)
//      success = buildSuccessResponse(rq, tx_json, sig, blobhex, hash, key.signPubKey)
//    } yield success
//
//    ok.leftMap(re => buildFailureResponse(rq, re))

  def signTxnSignature(txSig: TxnSignature, keyType: KeyType, seed: XrpSeed) = {
    val transactionSignature = txSig.signature
    keyType match
      case KeyType.secp256k1 =>
        val keyPair: XrpKeyPair = SecpKeyGenerators.createXrpKeyPair(seed)
      //  SecpSigning.sign(transactionSignature, keyPair)
      case KeyType.ed25519   =>
        val keyPair = Ed25519KeyGenerators.createXrpKeyPair(seed)
      //  Ed25519Signing.sign(transactionSignature, keyPair)
  }
//  def buildSuccessResponse(
//      rq: Json,
//      rqTxJson: Json,
//      sig: TxnSignature,
//      signedBlob: String,
//      hash: String,
//      signPubKey: String,
//  ): Json = {
//
//    val rsTxJson: Json = rqTxJson.mapObject { obj =>
//      obj
//        .remove("SigningPubKey")
//        .add("SigningPubKey", signPubKey.asJson)
//        .add("TxnSignature", Json.fromString(sig.hex))
//        .add("hash", Json.fromString(hash))
//    }
//
//    val sortedTxJson: Json = rsTxJson.mapObject(o => sortFieldsDroppingNulls(o))
//    val result             = JsonObject(("tx_blob" := signedBlob), ("tx_json" := sortedTxJson))
//
//    JsonObject
//      .singleton("id", findField("id", rq).toOption.asJson)
//      .add("result", result.asJson)
//      .add("status", Json.fromString("success"))
//      .add("type", Json.fromString("response"))
//      .asJson
//  }

  /**
    * Reminder to implement this somewhere, because even using a dedicated
    * Signing Server (instead of local code) we should do this
    */
  def autoFillTxnFields() = {
    //      _         <- validateAutofillFields(tx_json)
  }
}
