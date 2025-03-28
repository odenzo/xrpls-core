package com.odenzo.xrpl.models.data.models.memos

import cats.*
import cats.data.*
import cats.syntax.all.*
import scodec.bits.ByteVector

import java.nio.charset.{ Charset, StandardCharsets }

trait MemoBase {

  /** Throws IllegalArgumentException on problems */
  def fromXrpSubsetTextUnsafe(text: String, charset: Charset = StandardCharsets.UTF_8): ByteVector =
    fromXrpSubsetText(text, charset) match
      case Validated.Valid(a)   => a
      case Validated.Invalid(e) => throw IllegalArgumentException(s"Invalid Memo Data in  $text", e)

  /**
    * Creates a MemoFormat making sure that all char are english alphanumeric
    * plus a limited set of symbols. Doesn't impose length requirements.
    */
  def fromXrpSubsetText(
      s: String,
      charset: Charset = StandardCharsets.UTF_8,
  ): Validated[IllegalArgumentException, ByteVector] = {

    validXrpSubTextContent(s).andThen((s: String) => fromText(s, charset)) match
      case Validated.Valid(a: ByteVector) => Validated.Valid(a)
      case Validated.Invalid(e)           => Validated.Invalid(IllegalArgumentException(s"Invalid MemoData:$s Cause: $e"))

  }

  def fromTextUnsafe(s: String, charset: Charset) = fromText(s, charset) match
    case Validated.Valid(a)   => a
    case Validated.Invalid(e) => throw IllegalArgumentException(e)

  def fromText(s: String, charset: Charset): Validated[String, ByteVector] = {
    Validated.fromEither {
      ByteVector
        .encodeString(s)(charset).leftMap(e => s"Illegal CharEncodings in $s with encding $charset:: ${e.getMessage}")
    }
  }

  /**
    * Valid characters for the MemoType and MemoFormat fields. MemoData can be
    * anything
    */
  def validXrpSubTextContent(s: String): Validated[String, String] = {
    val invalidChars = s.flatMap { c => Option.unless(memoDomainSet.contains(c))(c) }
    val formatted    = invalidChars.map((c: Char) => s"[$c]").mkString(", ")
    Validated.cond(invalidChars.isEmpty, s, s"Invalid character(s): $formatted")
  }

  /** Alphabet of all valid characters for MemoType and MemoFormat No Spaces? */
  private val memoSubTextDomain =
    """ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!$&'()*+,;=%"""
  private val memoDomainSet     = memoSubTextDomain.toSet
}

object MemoBase extends MemoBase
