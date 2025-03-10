package com.odenzo.xrpl.models.data.models.memos

/**
  * Poor mans memo builder - threw away old one, quick have to get a simple text
  * memo
  */
object MemoBuilder {

  /** UTF-8 Encoded Memo */
  def simpleText(s: String): Memo = {
    Memo(
      MemoData.fromTextUnsafe(s),
      Some(MemoFormat.utf8TextFormat),
      None,
    )
  }

}
