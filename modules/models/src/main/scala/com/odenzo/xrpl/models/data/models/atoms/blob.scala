package com.odenzo.xrpl.models.data.models.atoms

import cats.implicits.*
import com.odenzo.xrpl.common.binary.ScodecExtensions.*
import com.odenzo.xrpl.common.binary.{ FixedSizeBinary, ScodecExtensions, XrpBinaryOps }
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.*
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.{ BitVector, ByteVector }

import scala.util.Try
// --- Note: These all get converted to Hex on JSON Encoding and Decoding
// --- The assumption is any markers/prefixes and checksum line in the hash iff they exist.
// LedgerHash over in Ledger file :-)

/**
  * Experiment with re-working structure of opaques. use `import blob.*` and
  * pray for no collisions.
  *
  * Not sure how to handle validators, currently just used in DIDSet
  */
object blob {
  opaque type Blob = BitVector

  object Blob:
    def validatedBits(bits: BitVector): Blob = bits

    def fromHex(str: String): Option[Blob] = BitVector.fromHex(str, HexUppercase)

  given Codec[Blob] = CirceCodecUtils.hexBitsCodec

  extension (b: Blob)
    def asBits: BitVector   = b
    def asBytes: ByteVector = b.bytes
}
