package com.odenzo.xrpl.models.data.ledgertree.nodes







//package com.odenzo.xrpl.models.ledgertree.nodes
//
//import io.circe.Decoder
//
//import com.odenzo.xrpl.models.atoms.{ AccountAddr, CurrencyAmount, LedgerIndex, RippleTime, TxnHash, TxnSequence }
//
///** See also docs for account root node. I am guessing this has delta too? */
//case class PayChannelNode(
//    flags: Option[Long],
//    account: Option[AccountAddress],
//    sequence: Option[TxnSequence],
//    takerPays: Option[CurrencyAmount],
//    takerGets: Option[CurrencyAmount],
//    bookDirectory: Option[AccountAddress],
//    bookNode: Option[UInt64], // really an option
//    expiration: Option[RippleTime],
//    ownerNode: Option[UInt64], // LedgerNodeIndex type.
//    previousTxnId: Option[TxnHash],
//    previousTxnLgrSeq: Option[LedgerIndex],
//    index: Option[String], // Guessing this is a LedgerNodeIndex of this node.
//) extends LedgerNode
//
//object PayChannelNode {
//
//  implicit val decode: Decoder[PayChannelNode] =
//    Decoder.forProduct12(
//      "Flags",
//      "Account",
//      "Sequence",
//      "TakerPays",
//      "TakerGets",
//      "BookDirectory",
//      "BookNode",
//      "Expiration",
//      "OwnerNode",
//      "PreviousTxnID",
//      "PreviousTxnLgrSeq",
//      "index",
//    )(PayChannelNode.apply)
//
//}
