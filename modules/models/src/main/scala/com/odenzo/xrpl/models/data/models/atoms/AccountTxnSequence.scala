package com.odenzo.xrpl.models.data.models.atoms

import io.circe.{ Decoder, Encoder }

/**
  * This is the sequence of a transaction local to the "on-account" (which signs
  * the txn) Give the account and TxnSequence can find. Name in JSON is usually
  * (?!) Sequence -- If this is set then it waits for that number to be
  * available? Allows propert sequencing of transactions on a per account level.
  */
case class AccountTxnSequence(v: Long) {

  /** Returns new TxnSequence with incremented value */
  def increment: AccountTxnSequence = AccountTxnSequence(v + 1)
}

object AccountTxnSequence {
  implicit val encoder: Encoder[AccountTxnSequence] = Encoder.encodeLong.contramap[AccountTxnSequence](_.v)
  implicit val decoder: Decoder[AccountTxnSequence] = Decoder.decodeLong.map(AccountTxnSequence(_))

}
