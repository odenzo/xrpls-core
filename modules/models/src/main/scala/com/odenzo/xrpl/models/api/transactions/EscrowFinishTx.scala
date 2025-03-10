package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import com.odenzo.ripple.localops.utils.CirceCodecUtils
//import com.odenzo.ripple.models.atoms.*
//import io.circe.*
//import io.circe.generic.semiauto.*
//
//case class EscrowFinishTx(
//    owner: AccountAddr,
//    offerSequence: TxnSequence,
//    condition: Option[String],
//    fulfillment: Option[String], // preimage-sha-256 crypto-condition.
//    base: CommonTx,
//) extends XRPLTx {
//
//  // val txnType: RippleTxnType = RippleTxnType.PaymentTxn
//
//}
//
//object EscrowFinishTx extends TxCodecs {
//
//  // Better to use mapJsonObject and derive encoder?
//  implicit val encoder: Encoder.AsObject[EscrowFinishTx] = {
//    deriveTxEncoder(deriveEncoder[EscrowFinishTx], "EscrowFinish")
//
//  }
//
//  /**
//    * This and other decoders for Txn has an "embedded" CommonTx at top level.
//    * TODO: Automate
//    */
//  implicit val decoder: Decoder[EscrowFinishTx] = Decoder.instance[EscrowFinishTx] { cursor =>
//    for {
//      acct    <- cursor.get[AccountAddress]("Owner")
//      owner   <- cursor.get[TxnSequence]("OfferSequence")
//      cond    <- cursor.get[Option[String]]("Condition")
//      fulfill <- cursor.get[Option[String]]("Fulfillment")
//      base    <- cursor.as[CommonTx]
//    } yield EscrowFinishTx(acct, owner, cond, fulfill, base)
//
//  }
//}
