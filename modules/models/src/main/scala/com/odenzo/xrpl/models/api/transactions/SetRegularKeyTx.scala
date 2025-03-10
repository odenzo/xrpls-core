package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import io.circe.generic.semiauto.deriveEncoder
//import io.circe.syntax.*
//import io.circe.{ Decoder, Json, ObjectEncoder }
//
///**
//  * @param account
//  *   Master account
//  * @param regularKey
//  *   Null to remove, otherwise the regular key address to bind to master
//  *   account/keys
//  */
//case class SetRegularKeyTx(account: AccountAddr, regularKey: Option[AccountAddress], base: CommonTx) extends XRPLTx
//
//object SetRegularKeyTx {
//
//  private val txType: RippleTxnType = RippleTxnType.SetRegularKey
//  private val tx: (String, Json)    = "TransactionType" -> txType.asJson
//
//  // Better to use mapJsonObject and derive encoder?
//  implicit val encoder: ObjectEncoder[SetRegularKeyTx] = {
//    deriveEncoder[SetRegularKeyTx]
//      .mapJsonObject(o => tx +: o)
//      .mapJsonObject(CommonTx.liftJsonObject(_, "base"))
//      .mapJsonObject(o => CommonTx.upcaseFields(o))
//  }
//  implicit val decoder: Decoder[SetRegularKeyTx]       = Decoder.instance[SetRegularKeyTx] { cursor =>
//    for {
//      acct       <- cursor.get[AccountAddress]("Account")
//      regularKey <- cursor.get[Option[AccountAddress]]("RegularKey")
//      base       <- cursor.as[CommonTx]
//    } yield SetRegularKeyTx(acct, regularKey, base)
//
//  }
//
//}
