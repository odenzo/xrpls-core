package com.odenzo.xrpl.models.data.models.memos

import cats.data.Validated
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import scodec.bits.ByteVector

import java.nio.charset.StandardCharsets

/**
  * Component of a Memo.
  *
  * @param hex
  *   Arbitrary length Hex (String) in Json
  */
opaque type MemoData <: ByteVector = ByteVector

object MemoData extends MemoBase {
  def fromByteVector(bv: ByteVector): MemoData         = bv
  def fromTextUnsafe(s: String): MemoData              = super.fromTextUnsafe(s, StandardCharsets.UTF_8)
  def fromText(s: String): Validated[String, MemoData] = super.fromText(s, StandardCharsets.UTF_8)

  given Codec[MemoData] = CirceCodecUtils.hexCodec

  extension (md: MemoData)
    def bv: ByteVector = md
    def len: Long      = md.size
}
