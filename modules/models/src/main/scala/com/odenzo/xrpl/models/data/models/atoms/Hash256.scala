package com.odenzo.xrpl.models.data.models.atoms

import cats.implicits.*
import com.odenzo.xrpl.common.binary.XrpBinOps.*
import com.odenzo.xrpl.common.binary.{ FixedSizeBinary, XrpBinOps, XrpBinaryOps }
import io.circe.{ Codec, Decoder, Encoder }
import scodec.bits.{ BitVector, ByteVector }

import scala.util.Try
// --- Note: These all get converted to Hex on JSON Encoding and Decoding
// --- The assumption is any markers/prefixes and checksum line in the hash iff they exist.
// LedgerHash over in Ledger file :-)

/** 256 bit hash. */

opaque type Hash256 = BitVector

object Hash256:
  def validatedBits(bits: BitVector)(using fsb: FixedSizeBinary[Hash256]): Hash256 = fsb.validated(bits): Hash256

  /**
    * I want to learn how to semi-automatic derive this, since we can't use
    * deriving on opaque type
    */
  given fsb: FixedSizeBinary[Hash256](256) with
    /** Raw functions with no validation at all, see validated */
    def fromBits(bits: BitVector): Hash256 = bits
    def toBits(a: Hash256): BitVector      = a: BitVector

  /**
    * Don't full understand what is going on here. Hash256 is erased to a
    * BitVector? I guess I should insist fromBits validates the length or is not
    * used, only validated is used by untrusted clients? Ideally toBits would do
    * the check too
    */

  given Decoder[Hash256] = fsb.decoderHex
  given Encoder[Hash256] = fsb.encoderHex
