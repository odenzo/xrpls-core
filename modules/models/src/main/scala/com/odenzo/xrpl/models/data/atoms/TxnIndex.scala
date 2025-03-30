package com.odenzo.xrpl.models.data.atoms

import io.circe.{ Decoder, Encoder }

/**
  * This stores the index of a transaction in a ledger. So, the 50th transaction
  * for an account in a new ledger might have an index of 4 But TxnSequence
  * would be 50.
  */
case class TxnIndex(v: Long) extends AnyVal

object TxnIndex {
  implicit val encoder: Encoder[TxnIndex] = Encoder.encodeLong.contramap[TxnIndex](_.v)
  implicit val decoder: Decoder[TxnIndex] = Decoder.decodeLong.map(TxnIndex(_))
}
