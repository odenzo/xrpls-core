package com.odenzo.xrpl.models.scodecs

import _root_.scodec.codecs.*
import scodec.*
import scodec.bits.*
import scodec.bits.Bases.Alphabets
import spire.math.ULong

trait BasicScodecs {

  /** I am not sure how picky XRPL is to upper or lower case hex. */
  protected def hexStringToBits(hex: String): Attempt[BitVector] = {
    val dres: Attempt[BitVector] = BitVector.fromHexDescriptive(hex.toUpperCase, Alphabets.HexUppercase) match {
      case Left(err) => Attempt.failure(Err(err))
      case Right(v)  => Attempt.successful(v)
    }
    dres
  }

  protected def bitsToHexString(bv: BitVector): Attempt[DecodeResult[String]] = {
    val dres = Attempt.successful(bv.toHex(Alphabets.HexUppercase))
    dres.map(DecodeResult(_, BitVector.empty))
  }

  /** Greedy Bits <-> Hex String */
  protected val xrphexAll: Codec[String] = Codec(hexStringToBits _, bitsToHexString)

  /**
    * Length in nibles where 1 nibble = 4 bits = 1/2 byte. Fixed length no VL
    * Encoding
    */
  protected def xrphex(lenInNibbles: Int): Codec[String] = {
    bitsStrict(lenInNibbles * 4).exmap[String](bitsToHex, hexStringToBits)
  }

  // Might want to make sure the bits are divisiable by four, now padded
  // size % 4 and leftPad
  protected def bitsToHex(bits: BitVector): Attempt[String] = Attempt.successful(bits.toHex(Alphabets.HexUppercase))

  protected def xrphash(bitLen: Int): Codec[String] = xrphex(bitLen / 4)

  /** Not VL Encoded */
  val xrphash128: Codec[String] = xrphash(128)

  /** VL Encoded? */
  val xrphash160: Codec[String] = xrphash(160)

  /** Not VL Encoded 256 bits of Hex String. */
  val xrphash256: Codec[String] = xrphash(256)

  /** Vector256 is always VL encoded and handled here */
  val xrpvectorhash256: Codec[String] = variableSizeBytes(VL.xrpvl, xrphash256)

  /** Blob which is always VL encoded, this handles VL */
  val xrpblob: Codec[String] = variableSizeBytes(VL.xrpvl, xrphexAll)

  val xrpuint8: Codec[Int] = uint8

  val xrpuint16: Codec[Int] = uint16

  val xrpuint32: Codec[Long] = uint32

  /** Handles 64bit Unsigned Long, but max value in practice is 63 bits? */
  val xrpulong64: Codec[Long] = uint32

  def xrpNIMP[T]: Codec[T]               = fail(Err("This isn't implemented yet"))
  def xrpError[T](msg: String): Codec[T] = fail[T](Err(msg))
}

object BasicScodecs extends BasicScodecs
