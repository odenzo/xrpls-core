package com.odenzo.xrpl.models.data.models.keys

import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.{ XrpBase58Fix, XrpBinaryOps }
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.xrpconstants.TypePrefix
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Codec
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.{ ByteVector, hex }

/**
  * Supports secp or ed25519 key types. secp is 33 bytes, and ed25519 is 32
  * bytes normalized to 33 by prepended 0xED Note that there is public_key_hex
  * and public_key is base58 format. The default codec is Base58
  *
  * We do not store the prefix or checksum. This will screw stuff up probably.
  * Normally I do it the other way around.
  */
opaque type XrpPublicKey = ByteVector

object XrpPublicKey extends XrpBinaryOps {

  private val log = LoggerFactory.getLogger

  /**
    * Expect a 33 byte secp256k1 or prefixed 0xED ed25519 key. 32 byte keys are
    * assumed to be ed25519 and 0xED prefix is added
    */
  def fromBytesUnsafe(bv: ByteVector): XrpPublicKey = {
    bv.size match
      case 33       => bv
      case 32       => hex"ED" ++ bv
      case otherLen => throw IllegalArgumentException(s"Invalid Size: $otherLen not 32 or 33")
  }

  def fromPublicKeyHex(hex: String): XrpPublicKey = fromPublicKeyBytes(ByteVector.fromValidHex(hex))

  /** Without the packaging */
  def fromPublicKeyBytes(bv: ByteVector): XrpPublicKey = fromBytesUnsafe(bv)

  def fromPublicKeyBase58(b58: String): XrpPublicKey = fromBytesUnsafe(
    XrpBinaryOps.fromXrpBase58Unsafe(b58).drop(1).dropRight(4)
  )

  def fromBase58Bytes(bv: ByteVector): Either[String, XrpPublicKey] =
    log.info(s"Decoding XrpPublicKey from B58: ${bv.toHex}")
    val unwrapped = XrpBinaryOps.unwrap(bv)
    validated(unwrapped).toEither.leftMap(_.foldSmash("Errors ->", ";", ""))
  // 23026e1ef2d320e25d810a608272e0f76f25f1bd318fafeca8c8f70ac996b3a4f59685d3f716
  //
  extension (ms: XrpPublicKey)
    def bv: ByteVector       = ms
    def asHex: String        = ms.toHex(HexUppercase)
    def base58: String       = XrpBase58Fix.toXrpBase58(ms: ByteVector)
    def isEd25519: Boolean   = ms.head == 0xed
    def isSecp256k1: Boolean = !isEd25519
    def wrapped: ByteVector  = XrpBinaryOps.wrap(TypePrefix.xrpPublicKey.prefix, ms)
    def asRawKey: ByteVector =
      ms.size match
        case 33 if ms.head == 0xed => ms.drop(1)
        case 33                    => ms
        case other                 => throw IllegalStateException(s"Malformed AccountPublicKey: $ms")

  def validated(ms: ByteVector): ValidatedNec[String, XrpPublicKey] = {
    (
      Validated.condNec(ms.size > 0, ms, s"XrpPublicKey cannot be zero length"),
      Validated.condNec(ms.size == 32 | ms.size == 33, ms, s"XrpPublicKey cannot be zero length"),
    ).mapN((_, _) => ms: XrpPublicKey)
  }

  /**
    * This should be applied to public_key field which is in Base58, when used
    * as Signing Key its Hex though
    */
  given base58Codec: Codec[XrpPublicKey] =
    CirceCodecUtils.xrpBase58Codec.iemap[XrpPublicKey](fromBase58Bytes)(wrapped)

}
