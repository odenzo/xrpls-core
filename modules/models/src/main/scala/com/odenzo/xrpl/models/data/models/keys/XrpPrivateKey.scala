package com.odenzo.xrpl.models.data.models.keys

import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import scodec.bits.Bases.Alphabets
import scodec.bits.ByteVector

/**
  * Supports secp or ed25519 key types. secp is 33 bytes, and ed25519 is 32
  * bytes normalized to 33 by prepended 0xED
  */
opaque type XrpPrivateKey = ByteVector

// Sight, this leaks out for IJ still
object XrpPrivateKey:

  /**
    * Expect a 33 byte secp256k1 or prefixed 0xED ed25519 key. 32 byte keys are
    * assumed to be ed25519 and 0xED prefix is added
    */
  def fromBytesUnsafe(bv: ByteVector): XrpPrivateKey = bv
  def fromHexUnsafe(hex: String): XrpPrivateKey      = fromBytesUnsafe(ByteVector.fromValidHex(hex))
  def fromBase58Unsafe(v: String)                    = XrpBinaryOps.fromXrpBase58Unsafe(v)

  /** This doesn't seem to be picked automatically and needs import? */
  extension (ms: XrpPrivateKey)
    def bv: ByteVector = ms
    def asHex: String  = ms.toHex(Alphabets.HexUppercase)
    def base58: String = XrpBinaryOps.toXrpBase58(ms: ByteVector)

  object Codecs:
    given Codec[XrpPrivateKey] = CirceCodecUtils.xrpBase58Codec
