package com.odenzo.xrpl.models.data.models.atoms

object TransactionRecord {}
//package com.odenzo.xrpl.models.atoms
//
//import io.circe.Decoder
//import io.circe.generic.semiauto.deriveDecoder
//
///**
//  * Models an individual transaction in the list of transactions returned by
//  * account_tx. Never actually seen a LedgerIndex as documented at:
//  * https://ripple.com/build/rippled-apis/#account-tx (scroll down a bit)
//  *
//  * LedgerIndex is optional, AccountTx doesn't return. even in TxNode?
//  */
//case class TransactionRecord(ledger_index: Option[LedgerIndex], meta: Meta, tx: TxNode, validated: Boolean)
//
//object TransactionRecord {
//
//  given decoder: Decoder[TransactionRecord] = deriveDecoder[TransactionRecord]
//}
