package com.odenzo.xrpl.communication.websocket.subscription

import cats.data.NonEmptyList
import cats.syntax.all.*
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import io.circe.derivation.{ Configuration, ConfiguredCodec }

import java.util.UUID

case class SubscribeRq(
    streams: Option[NonEmptyList[XrplStream]]              = None,
    accounts: Option[NonEmptyList[AccountAddress]]         = None,
    accountsProposed: Option[NonEmptyList[AccountAddress]] = None,
    books: Option[NonEmptyList[OrderBook]]                 = None,
    id: String                                             = UUID.randomUUID().toString,
    command: String                                        = "subscribe",
) derives ConfiguredCodec {
  def withID(id: String): SubscribeRq = this.copy(id = id)
}

object SubscribeRq {
  given Configuration = Configuration.default

  /** Get a message on each validated ledger, includes basic fee rates and such. */
  val validatedLedgers: SubscribeRq = SubscribeRq(streams = NonEmptyList.one(XrplStream.ledger).some)

  /**
    * When an account is on either side of a txn, and the txn is validated a
    * message is send. If accounts is empty then all txns in the validated
    * ledger? This is a good way to monitor completion of a transaction for an
    * account. This motivates future work to have one websocket with additional
    * subscribe/unsubscribe. But just a new websocket for account/txn and
    * closing it once the transaction is matched will do for now.
    */
  def validatedAccountTxns(accounts: Option[NonEmptyList[AccountAddress]]): SubscribeRq =
    SubscribeRq(streams = NonEmptyList.one(XrplStream.transactions).some, accounts = accounts)

  /** Monitor one or more OrderBooks...Bidder/Asker pair, */
  def bookChanges(orderBooks: NonEmptyList[OrderBook]): SubscribeRq =
    SubscribeRq(streams = NonEmptyList.one(XrplStream.book_changes).some, books = orderBooks.some)
}
