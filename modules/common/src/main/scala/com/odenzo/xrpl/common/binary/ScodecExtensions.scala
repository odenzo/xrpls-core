package com.odenzo.xrpl.common.binary

import _root_.scodec.bits.{ BitVector, ByteVector }
import cats.*

import java.security.MessageDigest

/** Generic (mostly) Exensions and implicits for ByteVector and BitVector */
object ScodecExtensions {

  given Conversion[BitVector, ByteVector] = _.bytes
  given Conversion[ByteVector, BitVector] = _.bits
  given Order[BitVector]                  = Order.fromOrdering[BitVector]
  extension (a: ByteVector)
    def stripPrefix(u: ByteVector): ByteVector             = u.drop(1)
    def stripChecksum(u: ByteVector): ByteVector           = u.dropRight(4)
    def unwrappedPropertyHandler: ByteVector => ByteVector = stripPrefix.andThen(stripChecksum)
    def base58Check(headerAndBody: ByteVector): ByteVector = headerAndBody ++ HashOps.sha256(headerAndBody).take(4)

  extension (a: BitVector)
    def stripPrefix(u: BitVector): BitVector             = u.drop(1 * 8)
    def stripChecksum(u: BitVector): BitVector           = u.dropRight(4 * 8)
    def unwrappedPropertyHandler: Any                    = stripPrefix.andThen(stripChecksum)
    def base58Check(headerAndBody: BitVector): BitVector =
      headerAndBody ++ HashOps.sha256(headerAndBody.bytes).take(4).bits

  given Monoid[BitVector] with {
    def empty: BitVector                               = BitVector.empty
    def combine(x: BitVector, y: BitVector): BitVector = x ++ y
  }

  given Monoid[ByteVector] with {
    def empty: ByteVector                                 = ByteVector.empty
    def combine(x: ByteVector, y: ByteVector): ByteVector = x ++ y
  }
}
