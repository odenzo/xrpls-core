package com.odenzo.xrpl.models.data.models.atoms

import io.circe.{ Decoder, Encoder }

/**
  * This is the sequence of a transaction local to the "on-account" (which signs
  * the txn) Give the account and TxnSequence can find. Name in JSON is usually
  * (?!) Sequence -- If this is set then it waits for that number to be
  * available? Allows propert sequencing of transactions on a per account level.
  */
case class AccountTxnNumber(v: Long) {

  /** Returns new TxnSequence with incremented value */
  def increment: AccountTxnNumber = AccountTxnNumber(v + 1)
}

object AccountTxnNumber {
  implicit val encoder: Encoder[AccountTxnNumber] = Encoder.encodeLong.contramap[AccountTxnNumber](_.v)
  implicit val decoder: Decoder[AccountTxnNumber] = Decoder.decodeLong.map(AccountTxnNumber(_))

}
