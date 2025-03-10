package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import com.odenzo.ripple.models.atoms.*
//import io.circe.*
//
///**
//  * Don't understand the correct use case for this. I create an Escrow (account
//  * to account) with cancel time, then try and cancel it before cancel time. Get
//  * no persmission. Wait until after cancel time expires and....
//  *
//  * @param account
//  * @param owner
//  * @param offerSequence
//  * @param base
//  */
//case class EscrowCancelTx(
//    account: AccountAddr, // Not neded?
//    owner: AccountAddr,
//    offerSequence: TxnSequence, // This is the sequence field in EscrowCreate
//    base: CommonTx,
//) extends XRPLTx {}
//
//object EscrowCancelTx {
//
//  import io.circe.generic.semiauto.*
//
//  final private val tx: (String, Json) = "TransactionType" -> Json.fromString("EscrowCancel")
//
//  given Encoder.AsObject[EscrowCancelTx] = {
//    deriveEncoder[EscrowCancelTx]
//      .mapJsonObject(o => tx +: o)
//      // FIXME:    .mapJsonObject(o => CommonTx.upcaseFields(o)) // Investigate
//      .mapJsonObject(o => CommonTx.liftBase(o))
//
//  }
//
//  implicit val decoder: Decoder[EscrowCancelTx] = Decoder.instance[EscrowCancelTx] { cursor =>
//    for {
//      acct     <- cursor.get[AccountAddress]("Account")
//      owner    <- cursor.get[AccountAddress]("Owner")
//      offerSeq <- cursor.get[TxnSequence]("OfferSequence")
//      base     <- cursor.as[CommonTx]
//    } yield EscrowCancelTx(acct, owner, offerSeq, base)
//
//  }
//
//}
