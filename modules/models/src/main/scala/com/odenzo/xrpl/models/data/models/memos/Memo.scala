package com.odenzo.xrpl.models.data.models.memos

import _root_.scodec.bits.{ BitVector, ByteVector }
import cats.Show
import cats.implicits.*
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.*
import io.circe.syntax.*

/**
  * One of the three fields is technically needed. This is the Ripple formatted
  * memo.
  *
  * MemoBase has some unpacking, but leave it to business layer to handle
  * application specific memos in general.
  *
  * @param memoData
  * @param memoFormat
  * @param memoType
  */
case class Memo(memoData: MemoData, memoFormat: Option[MemoFormat] = None, memoType: Option[MemoType] = None)
    derives ConfiguredCodec {

  def withType(t: MemoType): Memo     = copy(memoType = Some(t))
  def withFormat(f: MemoFormat): Memo = copy(memoFormat = Some(f))
  def withContent(d: MemoData): Memo  = copy(memoData = d)

}

object Memo extends MemoBase with CirceCodecUtils {
  given Configuration     = customConfiguration(capitalize)
  given Codec[ByteVector] = CirceCodecUtils.hexCodec
}
