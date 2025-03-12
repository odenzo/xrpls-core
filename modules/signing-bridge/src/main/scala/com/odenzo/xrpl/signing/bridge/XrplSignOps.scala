package com.odenzo.xrpl.signing.bridge

import com.odenzo.xrpl.common.binary.HashOps
import com.odenzo.xrpl.models.data.models.atoms.TxBlob
import scodec.bits.{ ByteVector, hex }

/** Non-Crypto ops needed to deal with Signing. The use the models. */

object XrplSignOps {
  def createResponseHashHex(txblob: TxBlob): String = {
    val transactionIdHashPrefix = hex"54584E00"
    // return new Hash256(sha512Half(HashPrefix.transactionID, serialized));
    val payload: ByteVector     = transactionIdHashPrefix ++ txblob.asByteVector
    val hashBytes: ByteVector   = HashOps.sha512Half(payload)
    val hex                     = hashBytes.toHex
    hex
  }
}
