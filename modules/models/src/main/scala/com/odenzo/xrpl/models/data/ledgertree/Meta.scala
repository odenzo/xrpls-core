package com.odenzo.xrpl.models.data.ledgertree







//package com.odenzo.xrpl.models.ledgertree
//
//import com.odenzo.ripple.localops.utils.CirceCodecUtils
//import com.odenzo.xrpl.models.atoms.TxnIndex
//import com.odenzo.xrpl.models.ledgertree.AffectedLedgerNode
//import com.odenzo.xrpl.models.support.TxnStatusCode
//import io.circe.*
//import io.circe.derivation.Configuration
//
///**
//  * The meta element is contained in results of following Ripple calls: X,Y,Z It
//  * has been tested with : account_tx only. The basic idea here is that entries
//  * have "delta"s of ledger nodes.
//  *
//  * @param affectedNodes
//  *   List of created, deleted or modified ledger nodes
//  * @param transactionIndex
//  *   The index of the transaction within the ledger it was committed.
//  * @param transactionResult
//  *   The Transaction Engine Result, e.g. tesSuccess. On succes its tesSUCCESS,
//  *   not sure error cases.
//  * @param delivered_amount
//  *   The actual amount delivered for money movements only. (TODO: CONFIRM THIS
//  *   EXISTS)
//  */
//case class Meta(
//    affectedNodes: List[AffectedLedgerNode],
//    transactionIndex: TxnIndex, // Is this index for account or for ledger?
//    transactionResult: TxnStatusCode,
//    delivered_amount: Option[CurrencyAmount],
//) derives Codec.AsObject
//
//object Meta:
//  given Configuration = CirceCodecUtils.capitalizeConfig(List("delivered_amount"))
