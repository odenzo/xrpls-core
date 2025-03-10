package com.odenzo.xrpl.models.data.ledgertree.nodes







//package com.odenzo.xrpl.models.ledgertree.nodes
//
//import io.circe.Decoder
//import com.odenzo.ripple.models.atoms.*
//import com.odenzo.xrpl.common.UInt64
//import com.odenzo.xrpl.models.atoms.{AccountAddr, CurrencyAmount, LedgerIndex, TxnHash, TxnSequence}
//
///**
//  * See also docs for account root node. I am guessing this has delta too?
//  *
//  */
//case class SignerListNode(
//    signerQuoruim: Option[AccountAddress],
//    signerEntries: Option[TxnSequence],
//    signerListID: Option[CurrencyAmount],
//    flags: Option[Long],
//    ownerNode: Option[UInt64], // LedgerNodeIndex type.
//    previousTxnId: Option[TxnHash],
//    previousTxnLgrSeq: Option[LedgerIndex],
//    index: Option[String] // Guessing this is a LedgerNodeIndex of this node.
//) extends LedgerNode
//
//object SignerListNode {
//
//  implicit val decode: Decoder[SignerListNode] =
//    Decoder.forProduct8(
//      "SignerQuorum",
//      "SignerEntries",
//      "SignerListID",
//      "Flags",
//      "OwnerNode",
//      "PreviousTxnID",
//      "PreviousTxnLgrSeq",
//      "index"
//    )(SignerListNode.apply)
//
//}
