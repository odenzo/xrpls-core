package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import io.circe.*
//import io.circe.syntax.*
//
///** Set Options on a Ripple Account */
//case class AccountSetTx(
//    account: AccountAddr,
//    setFlag: Option[AccountFlag],
//    clearFlag: Option[AccountFlag] = None,
//    transferRate: Option[Long]     = None,
//    base: CommonTx,
//) extends XRPLTx {}
//
//object AccountSetTx {
//
//  given Encoder.AsObject[AccountSetTx] = Encoder.AsObject.instance[AccountSetTx] { (v: AccountSetTx) =>
//    JsonObject(
//      "TransactionType" := "AccountSet",
//      "Account" := v.account,
//      "SetFlag" := v.setFlag,
//      "ClearFlag" := v.clearFlag,
//      "TransferRate" := v.transferRate,
//    ).deepMerge(v.base.asJsonObject)
//  }
//
//  implicit val decoder: Decoder[AccountSetTx] = Decoder.instance[AccountSetTx] { cursor =>
//    val result: Either[DecodingFailure, AccountSetTx] = for {
//      acct         <- cursor.get[AccountAddress]("Account")
//      setFlag      <- cursor.get[Option[AccountFlag]]("SetFlag")
//      clearFlag    <- cursor.get[Option[AccountFlag]]("ClearFlag")
//      transferRate <- cursor.get[Option[Long]]("TransferRate")
//      base         <- cursor.as[CommonTx]
//    } yield AccountSetTx(acct, setFlag, clearFlag, transferRate, base)
//
//    result
//  }
//}
