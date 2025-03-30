package com.odenzo.xrpl.models.data.atoms

import com.odenzo.xrpl.common.binary.FixedSizeBinary
import io.circe.Codec
import scodec.bits.BitVector
// --- Note: These all get converted to Hex on JSON Encoding and Decoding
// --- The assumption is any markers/prefixes and checksum line in the hash iff they exist.
// LedgerHash over in Ledger file :-)

/** 256 bit hash. */

opaque type Hash160 = BitVector

/**
  * I want to learn how to semi-automatic derive this, since we can't use
  * deriving on opaque type
  */
given FixedSizeBinary[Hash160](160) with
  def fromBits(bits: BitVector): Hash160 = bits

  def toBits(a: Hash160): BitVector = a

object Hash160 {
  given Codec[Hash160] = summon[FixedSizeBinary[Hash160]].hexCodec
}
