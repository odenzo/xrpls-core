package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import com.odenzo.ripple.models.atoms.RippleTxnType.PaymentChannelClaim
//import com.odenzo.ripple.models.atoms.{ Drops, PaymentChannelHash, RipplePublicKey, TxnHash }
//import io.circe.syntax.*
//import io.circe.{ Decoder, ObjectEncoder }
//
///**
//  *  Claim from a Payment Channel
//  * [[https://ripple.com/build/transactions/#paymentchannelclaim]]
//  *
//  *
//  */
//case class PaymentChannelClaimTx(channel: PaymentChannelHash,
//                                 balance: Option[Drops],
//                                 amount: Option[Drops],
//                                 signature: Option[TxnHash], // FIXME: Incorrect?
//                                 publicKey: Option[RipplePublicKey],
//                                 // FIXME: Two new flags to add.
//                                 base: CommonTx)
//    extends XRPLTx {}
//
//object PaymentChannelClaimTx {
//
//  implicit val encoder: ObjectEncoder[PaymentChannelClaimTx] = ObjectEncoder.instance[PaymentChannelClaimTx] { v =>
//    v.base.asJsonObject
//      .add("TransactionType", PaymentChannelClaim.entryName.asJson)
//      .add("Channel", v.channel.asJson)
//      .add("Balance", v.balance.asJson)
//      .add("Amount", v.amount.asJson)
//      .add("Signature", v.signature.asJson)
//      .add("PublicKey", v.publicKey.asJson)
//
//  }
//
//  implicit val decoder: Decoder[PaymentChannelClaimTx] = Decoder.instance[PaymentChannelClaimTx] { cursor =>
//    for {
//      channel <- cursor.get[PaymentChannelHash]("Channel")
//      balance <- cursor.get[Option[Drops]]("Balance")
//      amount  <- cursor.get[Option[Drops]]("Amount")
//      sig     <- cursor.get[Option[TxnHash]]("Signature")
//      pubkey  <- cursor.get[Option[RipplePublicKey]]("PublicKey")
//      base    <- cursor.as[CommonTx]
//    } yield PaymentChannelClaimTx(channel, balance, amount, sig, pubkey, base)
//  }
//}
