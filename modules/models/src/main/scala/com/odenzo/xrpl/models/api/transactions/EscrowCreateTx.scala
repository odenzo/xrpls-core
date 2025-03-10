package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import com.odenzo.ripple.models.atoms.*
//import com.odenzo.ripple.models.atoms.CurrencyAmount.Drops
//import io.circe.*
//
//case class EscrowCreateTx(
//    account: AccountAddr,
//    amount: Drops, // This amount is encoded as String in docs!
//    destination: AccountAddr,
//    cancelAfter: Option[RippleTime]    = None,
//    finishAfter: Option[RippleTime]    = None, // as above, need a new type for this.
//    condition: Option[String]          = None, // preimage-sha-256 crypto-condition.
//    destinationTag: Option[AccountTag] = None,
//    sourceTag: Option[AccountTag]      = None,
//    base: CommonTx,
//) extends XRPLTx {
//
//  // val txnType: RippleTxnType = RippleTxnType.PaymentTxn
//
//}
//
//object EscrowCreateTx {
//  private val tx: (String, Json) = "TransactionType" -> Json.fromString("EscrowCreate")
//
//  import io.circe.generic.semiauto.*
//  // Better to use mapJsonObject and derive encoder?
//  implicit val derivedEncoder: Encoder.AsObject[EscrowCreateTx] = {
//    deriveEncoder[EscrowCreateTx]
//      .mapJsonObject(o => tx +: o)
//      .mapJsonObject(o => CommonTx.liftBase(o))
//      .mapJsonObject(o => CommonTx.upcaseFields(o))
//
//    // Are all these upcased or just the Escrow Ones?
//  }
//
//  implicit val decoder: Decoder[EscrowCreateTx] = Decoder.instance[EscrowCreateTx] { cursor =>
//    for {
//      acct   <- cursor.get[AccountAddress]("Account")
//      owner  <- cursor.get[CurrencyAmount.Drops]("Amount")
//      dest   <- cursor.get[AccountAddress]("Destination")
//      cancel <- cursor.get[Option[RippleTime]]("CancelAfter")
//      finish <- cursor.get[Option[RippleTime]]("finishAfter")
//      cond   <- cursor.get[Option[String]]("Condition")
//      dt     <- cursor.get[Option[AccountTag]]("DestinationTag")
//      st     <- cursor.get[Option[AccountTag]]("SourceTag")
//      base   <- cursor.as[CommonTx]
//    } yield EscrowCreateTx(acct, owner, dest, cancel, finish, cond, dt, st, base)
//
//  }
//
//}
