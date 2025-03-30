package com.odenzo.xrpl.models.data.memos

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import scodec.bits.ByteVector

opaque type MemoFormat <: ByteVector = ByteVector

object MemoFormat extends MemoBase {
  export ByteVector.empty

  def fromByteVector(bv: ByteVector): MemoFormat = bv
  val utf8TextFormat: MemoFormat                 = super.fromXrpSubsetTextUnsafe("""text/plain;charset=UTF-8""")
  val jsonFormat: MemoFormat                     = super.fromXrpSubsetTextUnsafe("application/json")

  given Codec[MemoFormat] = CirceCodecUtils.hexCodec
  extension (md: MemoFormat)
    def bv: ByteVector = md
    def len: Long      = md.size
}
