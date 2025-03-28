package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.atoms.hash256.*
import io.circe.Codec
import scodec.bits.ByteVector

import scala.util.Try

case class LedgerHash(v: Hash256) extends AnyVal // Uses standard field name, might need to take out of here.

object LedgerHash {

  /**
    * LedgerHash is decoded and encoded from the directy enclosing field name,
    * e.g. ledger_hash or whatever. There may be cases where we want to specify
    * tagertFieldName = LedgerHash | LedgerHandle, not tried that out yet.
    */
  given ledgerHashCodec: Codec[LedgerHash] = {
    val hex2LedgerHash: ByteVector => Try[LedgerHash] = bv => Try(LedgerHash(Hash256.validatedBits(bv.bits)))
    val contraFnHex: LedgerHash => ByteVector         = (lh: LedgerHash) => lh.v.asBits.bytes
    // TODO: Be nice to have a predefined builder for these wrapper things.
    CirceCodecUtils.hexCodec.iemapTry[LedgerHash](hex2LedgerHash)(contraFnHex)
  }
}
