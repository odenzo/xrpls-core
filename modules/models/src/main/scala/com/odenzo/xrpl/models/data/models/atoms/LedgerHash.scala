package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import scodec.bits.ByteVector
import hash256.*
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
    val mapFn: Hash256 => Try[LedgerHash]             = (h256: Hash256) => Try(LedgerHash(h256))
    val contraFn: LedgerHash => Hash256               = (lh: LedgerHash) => lh.v
    val contraFnHex: LedgerHash => ByteVector         = (lh: LedgerHash) => lh.v.asBits.bytes
    // summon[Codec[Hash256]].iemapTry[LedgerHash](mapFn)(contraFn) // This exists but IJ can't deal with it right
    CirceCodecUtils.hexCodec.iemapTry[LedgerHash](hex2LedgerHash)(contraFnHex)
  }
}
