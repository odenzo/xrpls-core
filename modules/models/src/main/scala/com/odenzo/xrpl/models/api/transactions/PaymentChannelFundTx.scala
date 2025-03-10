package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import com.odenzo.ripple.models.atoms.RippleTxnType.PaymentChannelFund
//import io.circe.syntax.*
//import io.circe.{ Decoder, ObjectEncoder }
//
///**
//  * Funding an existing payment channel.
//  * [[https://ripple.com/build/transactions/#paymentchannelfund]]
//  */
//case class PaymentChannelFundTx(
//    account: AccountAddr,
//    channel: PaymentChannelHash,
//    amount: Drops = Drops.zero,
//    expiration: RippleTime,
//    base: CommonTx,
//) extends XRPLTx
//
//object PaymentChannelFundTx {
//
//  val txnType: RippleTxnType = PaymentChannelFund
//
//  implicit val encoder: ObjectEncoder[PaymentChannelFundTx] = ObjectEncoder.instance[PaymentChannelFundTx] { v =>
//    v.base.asJsonObject
//      .add("TransactionType", txnType.asJson)
//      .add("Channel", v.channel.asJson)
//      .add("Amount", v.amount.asJson)
//      .add("Expiration", v.expiration.asJson)
//
//  }
//
//  implicit val decoder: Decoder[PaymentChannelFundTx] = Decoder.instance[PaymentChannelFundTx] { cursor =>
//    for {
//      acct     <- cursor.get[AccountAddress]("Account")
//      channels <- cursor.get[PaymentChannelHash]("Channel")
//      amount   <- cursor.get[Drops]("Amount")
//      expires  <- cursor.get[RippleTime]("Expiration")
//      base     <- cursor.as[CommonTx]
//    } yield PaymentChannelFundTx(acct, channels, amount, expires, base)
//  }
//}
