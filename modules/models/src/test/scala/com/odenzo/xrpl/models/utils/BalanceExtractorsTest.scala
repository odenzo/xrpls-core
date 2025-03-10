//package com.odenzo.xrpl.models.utils
//
//import com.typesafe.scalalogging.StrictLogging
//import org.scalatest.FunSuite
//
//import com.odenzo.ripple.models.atoms.{Drops, FiatAmount}
//import com.odenzo.xrpl.models.atoms.{RippleTxnType, TransactionRecord, TxPayment}
//import com.odenzo.xrpl.models.wireprotocol.CodecTesting
//
///**
//  * Balance extractors are all tested in BasicTransactionsTest under the API module.
//  */
//class BalanceExtractorsTest extends FunSuite with CodecTesting with StrictLogging {
//
//  def balanceExtract(rec: TransactionRecord): List[AccountBalance] = {
//    val balances = BalanceExtractors.extractBalance(rec)
//    logger.debug(s"Balances: $balances")
//    balances
//  }
//  def isXrpPaymentSent(rec: TransactionRecord): Boolean = {
//    logger.debug(s"Tx Type ${rec.tx.txType}")
//    if (rec.tx.txType != RippleTxnType.Payment) false
//    else {
//      val txp = rec.tx.asInstanceOf[TxPayment]
//      txp.amount match {
//        case Drops(_)         => true
//        case FiatAmount(_, _) => false
//      }
//    }
//  }
//}
