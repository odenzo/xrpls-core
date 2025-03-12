package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.models.data.models.atoms.TxnSignature.txnSignatureHash
import scodec.bits.{ ByteVector, hex }

/** TxnSignature, without the HashPrefix */
case class TxnSignature(signature: ByteVector) {
  val withHashPrefix: ByteVector = txnSignatureHash ++ signature
}
object TxnSignature:
  val txnSignatureHash = hex"53545800"
