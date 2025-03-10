package com.odenzo.xrpl.models.scodecs

import scodec.*
import scodec.bits.*
import scodec.codecs.*

import scala.annotation.tailrec
import scala.util.Try

/**
  * FIXME TO USE EXISTING COMMON CODECS WITH FIXES FOR XRP Alphabet, this will
  * proxy to safe code changes
  */
trait RippleBase58Scodec {
  val xrplAlphabet: RippleBase58Alphabet = new RippleBase58Alphabet()

  /** Eager RippleBase58 scodec that will consume all the bytes */
  val xrplBase58: scodec.Codec[String] = scodec.Codec(fromBase58, toBase58)

  final def toRippleBase58(vector: BitVector): Attempt[String] = {
    toBase58(vector).map(_.value) // Throw away remaining as it consumes all
  }

  final def toBase58(bitsv: BitVector): Attempt[DecodeResult[String]] = {
    val bv = bitsv.bytes
    Attempt.fromTry {
      Try {
        val zeroChar = xrplAlphabet.toChar(0)
        if (bv.isEmpty) {
          DecodeResult("", BitVector.empty)
        }
        else {
          val ZERO  = BigInt(0)
          val RADIX = BigInt(58L)
          val ones  = List.fill(bv.takeWhile(_ == 0).length.toInt)(zeroChar)

          @tailrec
          def go(value: BigInt, chars: List[Char]): String = value match {
            case ZERO => (ones ++ chars).mkString
            case _    =>
              val (div, rem) = value /% RADIX
              go(div, xrplAlphabet.toChar(rem.toInt) +: chars)
          }

          val result = go(BigInt(1, bv.toArray), List.empty)
          DecodeResult(result, BitVector.empty)

        }
      }
    }

  }

  /** This is now in common */
  private def fromBase58(str: String): Attempt[BitVector] = {
    val alphabet   = new RippleBase58Alphabet()
    val zeroChar   = alphabet.toChar(0)
    val zeroLength = str.takeWhile(_ == zeroChar).length
    val zeroes     = ByteVector.fill(zeroLength.toLong)(0)
    val trim       = str.splitAt(zeroLength)._2.toList
    val RADIX      = BigInt(58L)
    Attempt.fromTry {
      Try {
        val decoded = trim.foldLeft(BigInt(0)) { (a, c) =>
          try {
            a * RADIX + BigInt(alphabet.toIndex(c))
          }
          catch {
            case e: IllegalArgumentException =>
              val idx = trim.takeWhile(_ != c).length
              throw new IllegalArgumentException(s"Invalid base 58 character '$c' at index $idx")
          }
        }
        if (trim.isEmpty) zeroes.bits
        else
          (zeroes ++ ByteVector(decoded.toByteArray.dropWhile(_ == 0))).bits
        // drop because toByteArray sometimes prepends a zero
      }
    }

  }
}

object RippleBase58Scodec extends RippleBase58Scodec
