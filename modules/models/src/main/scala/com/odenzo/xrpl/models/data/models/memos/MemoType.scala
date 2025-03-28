package com.odenzo.xrpl.models.data.models.memos

import cats.data.Validated
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import scodec.bits.ByteVector

import java.nio.charset.{ Charset, StandardCharsets }

/**
  * Hex value representing characters allowed in URLs. Conventionally, a unique
  * relation (according to RFC 5988) that defines the format of this memo.
  * Please see MemoType object
  */

opaque type MemoType = ByteVector

object MemoType extends MemoBase:
  val defaultMemo: MemoType  = unsafe("Default")
  val systemMemo: MemoType   = unsafe("com.odenzo/system")
  val merchantMemo: MemoType = unsafe("com.odenzo/merchant")
  val customerMemo: MemoType = unsafe("com.odenzo/customer")

  given Codec[MemoType] = CirceCodecUtils.hexCodec

  def unsafe(s: String, c: Charset = StandardCharsets.UTF_8): MemoType =
    super.fromXrpSubsetTextUnsafe("MemoType", c)

  def from(s: String, c: Charset = StandardCharsets.UTF_8): Validated[IllegalArgumentException, MemoType] =
    super.fromXrpSubsetText("MemoType", c)
