package com.odenzo.xrpl.models.data.ledgertree.nodes







//package com.odenzo.xrpl.models.ledgertree.nodes
//
//import io.circe.*
//
//import com.odenzo.xrpl.models.atoms.CurrencyAmount.Drops
//import com.odenzo.xrpl.common.UInt64
//import com.odenzo.xrpl.models.atoms.{ AccountAddr, AccountTag, LedgerIndex, RippleTime, TxnHash }
//import io.circe.derivation.Configuration
//
///**
//  * See also docs for account root node. I am guessing this has delta too?
//  * @param account
//  * @param flags
//  * @param sequence
//  * @param balance
//  * @param ownerCount
//  * @param prevTxnId
//  * @param prevTxnLgrSeq
//  * @param accountTxnId
//  * @param regularKeyAddr
//  * @param emailHash
//  * @param messagePubKey
//  * @param tickSize
//  * @param transferRate
//  * @param domain
//  * @param index
//  */
//case class EscrowNode(
//    account: Option[AccountAddress], // Think not optional
//    destination: Option[AccountAddress], // Think not optional
//    amount: Option[Drops],
//    condition: Option[String], // SHA256?
//    cancelAfter: Option[RippleTime],
//    finishAfter: Option[RippleTime],
//    sourceTag: Option[AccountTag],
//    destinationTag: Option[AccountTag],
//    flags: Option[Long],
//    ownerNode: Option[UInt64],
//    prevTxnId: Option[TxnHash], // The 64 character hex index (key)  , proper name? LedgerNodeIndex?  Cannot
//    prevTxnLgrSeq: Option[LedgerIndex], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
//    index: Option[String], // Guessing this is a LedgerNodeIndex of this node.
//) extends LedgerNode
//    derives Codec.AsObject
//
//object EscrowNode {
//  given Configuration = CirceCodecUtils.capitalizeConfig(List("Index"))
//
//}
