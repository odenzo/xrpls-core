package com.odenzo.xrpl.signing.bridge

import com.odenzo.xrp.bincodec.XrpBinCodecAPI
import com.odenzo.xrpl.models.api.commands.admin.Sign
import com.odenzo.xrpl.models.data.models.atoms.TxnSignature
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpKeyPair, XrpSeed }
import com.odenzo.xrpl.signing.core.ed25519.{ Ed25519KeyGenerators, Ed25519Signing }
import com.odenzo.xrpl.signing.core.passphases.PassphraseOps
import com.odenzo.xrpl.signing.core.secp256k1.{ SecpKeyGenerators, SecpSigning }
import scodec.bits.BitVector

/**
  * First cut is a stand-in for the normal Sign.Rq / Sign.Rs instead of sending
  * it to XRPL server.
  */
object OrchestrateSigningDev {

  /**
    * @param rq
    *   Full SignRq (SingingPubKey doesn't need to be filled)
    *
    * @return
    *   Signing Rs so it can be submmitted
    */
  def processSignRequest(rq: Sign.Rq) = {
    val txJson              = rq.txJson.toJson
    val encoded: BitVector  = XrpBinCodecAPI.encode(txJson, true)
    val signature           = TxnSignature(encoded.bytes)
    val keyType             = rq.keyType
    val secretSeed: XrpSeed = rq.seed.getOrElse {
      rq.passphrase match
        case Some(phrase) => convertPassphreaseToSeed(phrase)
        case None         => throw new IllegalArgumentException("No Passphrase or Seed in Sign Rq")
    }
    val signed: Unit        = signTxnSignature(signature, rq.keyType, secretSeed)
//      _         <- validateAutofillFields(tx_json)
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

  }

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

  /** COnverts a RFC 1751 or regular string passphrease into an XRP Seed */
  def convertPassphreaseToSeed(passphrase: String): XrpSeed = {
    PassphraseOps.seedFromString(passphrase)
  }
}
