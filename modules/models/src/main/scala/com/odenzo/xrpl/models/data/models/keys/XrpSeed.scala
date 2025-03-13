package com.odenzo.xrpl.models.data.models.keys

import cats.data.*
import cats.implicits.catsSyntaxEq
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Codec
import scodec.bits.Bases.Alphabets
import scodec.bits.{ ByteVector, hex }

import scala.util.Random

/**
  * This has inconsistency. Normally its the MasterSeed, which has a Prefix Byte
  * and 4 Byte Checksum. It can also be initialized from the MasterSeedHex
  * though, which doesn't have that. So, lets always just make it the core WITH
  * prefix and WITHOUT checksums. However, an ED25519 will have it padded with
  * `ED` as first byte.
  *
  * The Codec for this is master_seed as Base58
  */
opaque type XrpSeed <: ByteVector = ByteVector

object XrpSeed:
  private val log = LoggerFactory.getLogger

  val typePrefix: Byte         = 0x21
  val typePrefixBV: ByteVector = hex"21"
  val bodyLengthInBytes: Int   = 21 // Including Prefix Byte and 4 byte checksum
  val coreLengthInBytes: Int   = 16

  /**
    * This is problematic because its spitting out unicode which stalls the
    * loggs, and somehow is screwing up something else. (when doing random
    * String)
    */
  def randomPassphrase: String = Random.nextLong().toString

  /**
    * Validate the wrapped bytevector, including any prefixes etc. This expects
    * the full 21 bytes instead of 16, inconsistent, fix it up still by pruning
    * down to ED only padding. No checksum
    */
  def validated(bv: ByteVector): ValidatedNec[String, XrpSeed] =
    (
      Validated.condNec(bv.head == typePrefix, bv, s"XrpSeed Byte Prefix ${bv.head} didnt Match $typePrefix"),
      Validated.condNec(bv.size === bodyLengthInBytes,
                        bv,
                        s""" MasterSeed/XrpSeed should be ${bodyLengthInBytes - 4}   but was ${bv.size}""".stripMargin,
                       ),
    ).mapN((_, _) => bv: XrpSeed)

  def wrap(seed: XrpSeed): ByteVector = XrpBinaryOps.xrpChecksum(ByteVector(typePrefix) ++ seed)

  /**
    * Bytes given `b` get typePrefix pre-pended, but the checksum is not
    * appended. Essentially the given `b`
    */
  def fromBytesUnsafe(b: ByteVector): XrpSeed =
    assert(b.size == 16)
    ByteVector(typePrefix) ++ b

  /** TODO: Add Validation THis will bve tge full typeCode + body + checksum */
  def fromMasterSeedBase58(b58: String): XrpSeed =
    val full: ByteVector = XrpBinaryOps.fromXrpBase58Unsafe(b58).drop(1).dropRight(4) // Will have field and and checkum
    log.trace(s"Decoded [$b58] to $full")
    full: XrpSeed

  def fromMasterSeedHex(s: String): XrpSeed =
    val bytes = ByteVector.fromValidHex(s)
    fromBytesUnsafe(bytes)

  // leftMap Function worthy of a utility
  def attemptFrom(bv: ByteVector): Either[String, XrpSeed] =
    log.trace(s"Attempting to decode XrpSeed attemptFrom Validation Phase: $bv")
    validated(bv).toEither.leftMap(errs => errs.foldSmash("Errors -> ", ";", "<-"))

  given base58: Codec[XrpSeed] =
    CirceCodecUtils.xrpBase58Codec.iemap((in: ByteVector) => attemptFrom(in))((seed: XrpSeed) => wrap(seed))

  extension (ms: XrpSeed)
    /** Byte Vector including any prefix or checksum */
    def bv: ByteVector    = ms
    def asKey: ByteVector = bv

    /** Returns in master_seed_hex format which has no checksum and no prefix. */
    def asMasterSeedHex: String = ms.drop(1).toHex(Alphabets.HexUppercase)

end XrpSeed
