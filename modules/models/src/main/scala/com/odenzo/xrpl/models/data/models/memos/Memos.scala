package com.odenzo.xrpl.models.data.models.memos

import cats.data.NonEmptyList
import cats.implicits.*
import io.circe.*

/**
  * Another bit of a dogs breakfast. This whole memo heirarchy
  *
  * Memos contains zero or more memos. The total size in bytes of memos in
  * Ripple in constrained. (1K, 700K?)
  *
  * @param mList
  */
case class Memos(memos: NonEmptyList[Memo]) {
  def add(m: Memo): Memos = this.copy(memos = this.memos.prepend(m))
}

object Memos {

  /** Full fledge memo */
  def fromMemo(m: Memo): Memos                = Memos(NonEmptyList.one(m))
  def fromMemos(m: NonEmptyList[Memo]): Memos = Memos(m)

  // def fromText(s: String): Option[Memos] = Some(Memos(List(Memo.fromText(s))))

  given decoder: Decoder[Memos] = Decoder.decodeNonEmptyList[Memo].map(x => Memos(x))

  given Encoder[Memos] = Encoder.encodeNonEmptyList[Memo].contramap(_.memos)

}
