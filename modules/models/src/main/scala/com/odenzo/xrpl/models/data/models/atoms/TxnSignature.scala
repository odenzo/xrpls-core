package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.atoms.TxnSignature.txnSignatureHash
import io.circe.Codec
import scodec.bits.{ ByteVector, hex }

import scala.util.Try

/** TxnSignature, without the HashPrefix */
case class TxnSignature(signature: ByteVector) {
  val withHashPrefix: ByteVector = txnSignatureHash ++ signature
}
object TxnSignature:
  val txnSignatureHash = hex"53545800"

  given Codec[TxnSignature] = CirceCodecUtils.hexCodec.iemapTry(bv => Try(TxnSignature(bv)))(_.signature)
