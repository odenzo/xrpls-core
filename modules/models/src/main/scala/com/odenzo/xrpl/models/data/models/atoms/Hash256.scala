package com.odenzo.xrpl.models.data.models.atoms

import cats.Order
import cats.implicits.*
import com.odenzo.xrpl.common.binary.ScodecExtensions.*
import com.odenzo.xrpl.common.binary.{ FixedSizeBinary, ScodecExtensions, XrpBinaryOps }
import io.circe.*
import scodec.bits
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.{ BitVector, ByteVector }

import scala.util.Try
// --- Note: These all get converted to Hex on JSON Encoding and Decoding
// --- The assumption is any markers/prefixes and checksum line in the hash iff they exist.
// LedgerHash over in Ledger file :-)

/** 256 bit hash. */
object hash256 {
  opaque type Hash256 = BitVector

  object Hash256:
    def validatedBits(bits: BitVector)(using fsb: FixedSizeBinary[Hash256]): Hash256 = fsb.validated(bits): Hash256

    def fromHex(str: String): Option[Hash256] = BitVector.fromHex(str, HexUppercase)

  end Hash256

  /**
    * I want to learn how to semi-automatic derive this, since we can't use
    * deriving on opaque type
    */
  given fsb: FixedSizeBinary[Hash256](256) with

    /** Raw functions with no validation at all, see validated */
    def fromBits(bits: BitVector): Hash256 = bits

    def toBits(a: Hash256): BitVector = a: BitVector

  /**
    * Don't full understand what is going on here. Hash256 is erased to a
    * BitVector? I guess I should insist fromBits validates the length or is not
    * used, only validated is used by untrusted clients? Ideally toBits would do
    * the check too
    */

  given decoder: Decoder[Hash256] = fsb.decoderHex

  given encoder: Encoder[Hash256] = fsb.encoderHex

  given KeyEncoder[Hash256] = KeyEncoder.instance[Hash256](hash => fsb.convertToHex(hash))

  given KeyDecoder[Hash256] = KeyDecoder.instance[Hash256](str => Hash256.fromHex(str))

  given Order[Hash256] = summon[Order[BitVector]]
}
