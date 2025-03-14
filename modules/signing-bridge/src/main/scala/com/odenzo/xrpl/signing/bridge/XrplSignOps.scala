//package com.odenzo.xrpl.signing.bridge
//
//import com.odenzo.xrpl.common.binary.HashOps
//import com.odenzo.xrpl.common.xrpconstants.TxHashPrefix
//import com.odenzo.xrpl.models.data.models.atoms.{TxBlob, TxnSignature}
//import com.odenzo.xrpl.models.data.models.keys.SigningKey
//import io.circe.Json
//import scodec.bits.{ByteVector, hex}
//
///** Non-Crypto ops needed to deal with Signing. The use the models. */
//
//object XrplSignOps {
//  def createResponseHashHex(txblob: TxBlob): String = {
//    val transactionIdHashPrefix = hex"54584E00"
//    // return new Hash256(sha512Half(HashPrefix.transactionID, serialized));
//    val payload: ByteVector     = transactionIdHashPrefix ++ txblob.asByteVector
//    val hashBytes: ByteVector   = HashOps.sha512Half(payload)
//    val hex                     = hashBytes.toHex
//    hex
//  }
//
//  /**
//    * This doesn't actually sign, just generates the TxBlob given TxnSignature.
//    * The SigningPubKey and everything is in the tx_json already.
//    *
//    * @param tx_json
//    *   Filled tx_json, including SingingPubKey
//    * @param txnSignature
//    *   In Hex format, Empty String when multisigning.
//    * @return
//    *   Updated tx_blob in hex form for use in Submit call.
//    */
//  def createSignedTxBlob(tx_json: Json, txnSignature: TxnSignature): Either[LocalOpsError, Array[Byte]] = {
//    // Could add the HashPrefix. and get the hash if needed, e.g. to recreate SignRs message
//    val withSig = tx_json.mapObject(_.add("TxnSignature", Json.fromString(txnSignature.hex)))
//    BinCodecProxy.serialize(withSig)
//  }
//  //  /**
//  //    * Has this been thoroughly tested? Should be 32 bytes TODO: Broken! And not
//  //    * always used as part of calc hash logic
//  //    *
//  //    * @param txblob
//  //    *   Is this a SigningTxBlob or all TxBlob
//  //    *
//  //    * @return
//  //    *   Calculates a response objects Hash (in hex) from tx_blob
//  //    */
//  //  def createResponseHashHex(txblob: Array[Byte]): String = {
//  //    // return new Hash256(sha512Half(HashPrefix.transactionID, serialized));
//  //    val payload: Seq[Byte]          = HashPrefix.transactionID.asBytes ++ txblob
//  //    val hashBytes: IndexedSeq[Byte] = HashOps.sha512Half(payload.toArray)
//  //    val hex                         = bytes2hex(hashBytes)
//  //    hex
//  //  }
//
//  /**
//    * This does the binary serialize for signing (only isSigning fields), adds
//    * TransactionSig Prefix then signs.
//    *
//    * @param tx_json
//    *   Filled tx_json, including SingingPubKey
//    * @return
//    *   TxnSignature
//    */
//  def signToTxnSignature(encodedTxJson: ByteVector, txJson: Json, key: SigningKey): Any = {
//    val payload = TxHashPrefix.transactionSig.bv ++ encodedTxJson
//
//    // for {
//    //    val hex: Either[LocalOpsError, String] = key match {
//    //      case edKey: SigningKeyEd25519   => ED25519CryptoBC.sign(payload, edKey.kp).map(bytes2hex)
//    //      case secpKey: SigningKeySecp256 =>
//    //        val hashed = HashOps.sha512Half(payload)
//    //        Secp256K1CryptoBC.sign(hashed.toArray, secpKey.kp).map(b => b.toHex)
//    //    }
//    //    hex.map(TxnSignature)
//
//    // ans     <- signPayload(payload, key)
//    // } yield ans
//
//  }
//}
