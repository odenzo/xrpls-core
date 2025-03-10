package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import io.circe.syntax.*
//import io.circe.{ Decoder, ObjectEncoder }
//
//object PaymentChannelCreate {
//
//  /**
//    * Funding an existing payment channel.
//    * [[https://ripple.com/build/transactions/#paymentchannelcreate]]
//    */
//  case class Rq(
//      account: AccountAddr,
//      amount: CurrencyAmount.Drops,
//      destination: AccountAddr,
//      settleDelay: UInt32, // ? Duration in what? seconds I assume.  Ex 1 day in seconds
//      publicKey: RipplePublicKey,
//      cancelAfter: Option[RippleTime],
//      destinationTag: Option[AccountTag],
//      sourceTag: Option[AccountTag],
//      base: CommonTx,
//  ) extends XRPLTx {}
//
//  object Rq {
//
//    implicit val encoder: ObjectEncoder[PaymentChannelCreateTx] = ObjectEncoder.instance[PaymentChannelCreateTx] { v =>
//      v.base.asJsonObject
//        .add("TransactionType", (PaymentChannelCreate: RippleTxnType).asJson)
//        .add("Account", v.account.asJson)
//        .add("Amount", v.amount.asJson)
//        .add("Destination", v.destination.asJson)
//        .add("SettleDelay", v.settleDelay.asJson)
//        .add("PublicKey", v.publicKey.asJson)
//        .add("CancelAfter", v.cancelAfter.asJson)
//        .add("DestinationTag", v.destinationTag.asJson)
//        .add("SourceTag", v.sourceTag.asJson)
//
//    }
//
//    implicit val decoder: Decoder[PaymentChannelCreateTx] = Decoder.instance[PaymentChannelCreateTx] { cursor =>
//      for {
//        acct   <- cursor.get[AccountAddress]("Account")
//        amount <- cursor.get[Drops]("Amount")
//        dest   <- cursor.get[AccountAddress]("Destination")
//        settle <- cursor.get[UInt32]("SettleDelay")
//        pubkey <- cursor.get[RipplePublicKey]("PublicKey")
//        cancel <- cursor.get[Option[RippleTime]]("CancelAfter")
//        dtag   <- cursor.get[Option[AccountTag]]("DestinationTag")
//        stag   <- cursor.get[Option[AccountTag]]("SourceTag")
//
//        base <- cursor.as[CommonTx]
//      } yield PaymentChannelCreateTx(acct, amount, dest, settle, pubkey, cancel, dtag, stag, base)
//    }
//  }
//}
