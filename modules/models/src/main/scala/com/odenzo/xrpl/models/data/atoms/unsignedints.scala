package com.odenzo.xrpl.models.data.atoms

import com.tersesystems.blindsight.LoggerFactory
import io.circe.{ Decoder, Encoder }
import scodec.bits.BitVector
import spire.math.{ UInt, ULong, UShort }

import java.math.BigInteger
import scala.util.Try
import scala.util.chaining.given

/**
  * We have Spire UInt, ULong etc which is fine, but we can overflow them on
  * construction. Playing with "tags" and thiunjing go back to Singleton
  * literals. e.g. UInt[8] but then have trouble with UInt64, which I don't
  * think is actually used except for some ledger stuff? Why not use spure?
  * Because it is unsafe on overflow, boo! This is a total wack job as
  * experiments failed. But we use it (sparingly) for now anyway.
  * @tparam A
  */
trait UnsignedIntTag[A](val fixedSize: Int) {

  private val log = LoggerFactory.getLogger

  def unsignedBitsToLong(a: BitVector): Long = a.toLong(false)
  def signedBitsToLong(a: BitVector): Long   = a.toLong(true)

  val maxUnsignedValue: BigInt                         = BigInt(2).pow(fixedSize) - 1
  val (minSignedValue: BigInt, maxSignedValue: BigInt) = {
    val base = BigInt(2).pow(fixedSize - 1)
    (-base, base - 1)
  }
  lazy val unsignedFitsInLong: Boolean                 = maxUnsignedValue.isValidLong
  lazy val signedFitsInLong: Boolean                   = maxUnsignedValue.isValidLong

  def fromBigInt(bi: BigInt): ULong = validateBigInt(bi).pipe(v => ULong.fromBigInt(v))

  /**
    * This validates the Long as unsigned not 2s completement. Note that ULong
    * take a 2s complement version. which is the same
    */
  def fromULong(l: Long): ULong = validateULong(l).pipe(v => ULong.fromLong(v))

  /**
    * Expects postive signed long value for larger unsigned long values that
    * exceed Long capactity use fromBigInt
    */
  def validateULong(l: Long): Long = if unsignedFitsInLong || l <= maxUnsignedValue && l >= 0 then l
  else throw IllegalArgumentException(s"$l > $maxUnsignedValue or negative")

  /**
    * This can take a Long in 2s-completement. Fits in all ULong and smaller.
    * For smaller we check maxValue
    */
  def validateLong(l: Long): Long = if unsignedFitsInLong || l <= maxUnsignedValue && l >= 0 then l
  else throw IllegalArgumentException(s"$l > $maxUnsignedValue or negative")

  def validatedLong(l: Long): Try[Long] = Try(validateLong(l))

  def validateBigInt(bi: BigInt): BigInt = {
    log.debug(s"Validating Bit-Int BigInt against fixed size $fixedSize $maxUnsignedValue :: $bi")
    if bi <= maxUnsignedValue && bi.sign.toInt > -1 then bi
    else throw IllegalArgumentException(s"$bi > $maxUnsignedValue or negative")
  }
}

opaque type UInt64 = ULong

object UInt64 extends UnsignedIntTag[UInt64](64) {
  override def fromBigInt(bi: BigInt): UInt64      = super.fromBigInt(bi): UInt64
  def fromValidatedBigInt(bi: BigInt): Try[UInt64] = Try(super.fromBigInt(bi): UInt64)

  /** Accepts 2s complement (by converting to unsigned) or unsigned long */
  def fromLong(v: Long): UInt64 = ULong(v)

  extension (u: UInt64)
    def unwrap: ULong        = u: ULong
    def asBigInt: BigInteger = u.bigInteger
    def asLong: Option[Long] = Option.when(u.isValidLong)(u.toLong)

}
opaque type UInt32 = ULong

object UInt32 extends UnsignedIntTag[UInt32](32) {

  /** Accepts positive Long value less than Int.MAX */
  def fromLong(v: Long): UInt                 = validateULong(v).pipe((v: Long) => UInt.apply(v))
  def fromValidatedLong(v: Long): Try[UInt32] = validatedLong(v).map((v: Long) => ULong(v))

  extension (u: UInt32)
    def unwrap: ULong = u: ULong
    def asLong: Long  = u.toLong

}

opaque type UInt16 = UShort

object UInt16 extends UnsignedIntTag[UInt16](16) {
  def fromLong(v: Long): UShort = validateULong(v).pipe((v: Long) => UShort.apply(v.toInt))
  def fromInt(vi: Int): UShort  = validateULong(vi.toLong).pipe((v: Long) => UShort.apply(v.toInt))

  def fromValidatedLong(v: Long): Try[UInt16] = validatedLong(v).map((v: Long) => UShort(v.toShort))
  extension (u: UInt16)
    def unwrap: UShort = u: UShort
    def asLong: Long   = u.toLong

}

opaque type UInt8 = UShort

object UInt8 extends UnsignedIntTag[UInt8](8) {

  def fromInt(vi: Int): UShort = validateULong(vi.toLong).pipe((v: Long) => UShort.apply(v.toInt))

  def fromValidatedLong(v: Long): Try[UInt8] = validatedLong(v).map((v: Long) => UShort(v.toShort))
  extension (u: UInt8)
    def unwrap: UShort = u: UShort
    def asLong: Long   = u.toLong

}

opaque type UInt4 = UShort

object UInt4 extends UnsignedIntTag[UInt4](4) {

  def fromInt(vi: Int): UShort = validateULong(vi.toLong).pipe((v: Long) => UShort.apply(v.toInt))

  def fromValidatedLong(v: Long): Try[UInt4] = validatedLong(v).map((v: Long) => UShort(v.toShort))

  extension (u: UInt4)
    def unwrap: UShort = u: UShort
    def asLong: Long   = u.toLong
}

object UnsignedCodecs {
  given Encoder[UInt64] = Encoder.encodeBigInt.contramap[UInt64](_.asBigInt)

  given Encoder[UInt32] = Encoder.encodeLong.contramap[UInt32](_.asLong)

  given Encoder[UInt16] = Encoder.encodeLong.contramap[UInt16](_.asLong)

  given Encoder[UInt8] = Encoder.encodeLong.contramap[UInt8](_.asLong)

  given Encoder[UInt4] = Encoder.encodeLong.contramap[UInt4](_.asLong)

  given Decoder[UInt64] = Decoder.decodeBigInt.emapTry[UInt64](UInt64.fromValidatedBigInt)

  given Decoder[UInt32] = Decoder.decodeLong.emapTry[UInt32](UInt32.fromValidatedLong)

  given Decoder[UInt16] = Decoder.decodeLong.emapTry[UInt16](UInt16.fromValidatedLong)

  given Decoder[UInt8] = Decoder.decodeLong.emapTry[UInt8](UInt8.fromValidatedLong)

  given Decoder[UInt4] = Decoder.decodeLong.emapTry[UInt4](UInt4.fromValidatedLong)
}
//
//object UInt64 {}
//
///** For BitVectors (Hex) serialization */

//
//abstract class UnsignedIntTag(bitLen: Int) extends XrpType {
//  def bits: BitVector
//
//  val maxValue: BigInt         = BigInt(2).pow(bitLen) - 1
//  lazy val fitsInLong: Boolean = maxValue.isValidLong
//
//  /** Make sure the long is valid for the bit length defined * */
//  def validateULong(l: Long): Long = if fitsInLong && l >= 0
//  then l
//  else throw IllegalArgumentException(s"$l > $maxValue or negative")
//
//  def validateBigInt(bi: BigInt): BigInt =
//    if bi > maxValue || bi.sign == -1 then throw IllegalArgumentException(s"$bi > $maxValue or negative") else bi
//}
//
//object UnsignedIntTag {
//  val encodeBitsAsULong: Encoder[BitVector] =
//    Encoder.encodeLong.contramap[BitVector]((bv: BitVector) => bv.toLong(false))
//
//  val encodeBitsAsBigInt: Encoder[BitVector] =
//    Encoder.encodeBigInt.contramap[BitVector]((bv: BitVector) => BigInt(bv.toByteArray))
//
//  val encodeAsBitVectorHex: Encoder[BitVector] =
//    Encoder.encodeString.contramap[BitVector]((bv: BitVector) => bv.toHex)
//
//  val encodeUIntAsHex: Encoder[UnsignedIntTag]    = encodeAsBitVectorHex.contramap[UnsignedIntTag](x => x.bits)
//  val encodeUIntAsBigInt: Encoder[UnsignedIntTag] = encodeBitsAsBigInt.contramap[UnsignedIntTag](x => x.bits)
//  val encodeUIntAsLong: Encoder[UnsignedIntTag]   = encodeBitsAsULong.contramap[UnsignedIntTag](x => x.bits)
//
//  // object BitTypes:
////
////    import scala.compiletime.*
////
////    val UINT4: 4   = 4
////    val UINT8: 8   = 8
////    val UINT16: 16 = 16
////    val UINT32: 32 = 32
////
////  /** Here T is supposed to be an Int literal/singleton Type */
////  case class DynaBits[T <: Singleton](bv: BitVector)(using ValueOf[T]) extends XrpType {
////    final val size: T = valueOf[T]
////  }
//
//  // val xrp: DynaBits[UInt8.type] = DynaBits[UInt8.type](BitVector.empty)
//
//  case class UInt8(bits: BitVector) extends UnsignedIntTag(8)
//
//  case class UInt16(bits: BitVector) extends UnsignedIntTag(16) {}
//
//  case class UInt32(bits: BitVector) extends UnsignedIntTag(32) {}
//
//}
