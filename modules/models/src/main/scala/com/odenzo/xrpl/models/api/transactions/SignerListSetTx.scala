package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import com.odenzo.ripple.models.atoms.RippleTxnType.SignerListSet
//import com.odenzo.ripple.models.atoms.{ AccountAddr, SignerEntry }
//import io.circe.generic.semiauto.*
//import io.circe.syntax.*
//import io.circe.{ Decoder, ObjectEncoder }
//
///** For setting or removing Multi-Signature List.
//  * The general idea here is to disable PartyA account I guess.
//  * [[https://ripple.com/build/transactions/#signerlistset]]
//  * TODO: Multisig  needs testing
//  * @param account
//  * @param signerQuorum
//  * @param signerEntries  Entries, omitted when deleting.
//  * @param base
//  */
//case class SignerListSetTx(account: AccountAddr,
//                           signerQuorum: Int,
//                           signerEntries: Option[List[SignerEntry]],
//                           base: CommonTx)
//    extends XRPLTx {}
//
//object SignerListSetTx {
//  private case class DummyWrapper(SignerEntry: SignerEntry)
//  private object DummyWrapper {
//    implicit val decoder: Decoder[DummyWrapper]       = deriveDecoder[DummyWrapper]
//    implicit val encoder: ObjectEncoder[DummyWrapper] = deriveEncoder[DummyWrapper]
//  }
//
//  implicit val encoder: ObjectEncoder[SignerListSetTx] = ObjectEncoder.instance[SignerListSetTx] { v =>
//    v.base.asJsonObject
//      .add("TransactionType", SignerListSet.entryName.asJson)
//      .add("Account", v.account.asJson)
//      .add("SignerQuorum", v.signerQuorum.asJson)
//      .add("SignerEntries", v.signerEntries.asJson)
//
//  }
//
//  implicit val decoder: Decoder[SignerListSetTx] = Decoder.instance[SignerListSetTx] { cursor =>
//    for {
//      acct    <- cursor.get[AccountAddress]("Account")
//      quorum  <- cursor.get[Int]("SignerQuorum")
//      entries <- cursor.get[Option[List[SignerEntry]]]("SignerEntries")
//      base    <- cursor.as[CommonTx]
//    } yield SignerListSetTx(acct, quorum, entries, base)
//
//  }
//
//}
