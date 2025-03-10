package com.odenzo.xrpl.communication.pending

//import com.odenzo.ripple.localops.utils.CirceCodecUtils
//import io.circe.derivation.Configuration
//
//import scala.collection.immutable
//import io.circe.generic.semiauto.*
//import io.circe.syntax.*
//import io.circe.*
//
///**
//  * Used for the actual signing of a transaction.
//  *
//  * @param account
//  * @param signingPubKey
//  * @param txnSignature
//  */
//case class Signer(account: AccountAddr, signingPubKey: RipplePublicKey, txnSignature: TxnSignature)
//    derives Codec.AsObject
//
//object Signer:
//
//  given Configuration = CirceCodecUtils.capitalizeConfig
//
///**
//  * The signers appear in tx_json elements (RippleTransaction in via CommonTx).
//  * Signer is array os JsonObjects each with exactly one Signer object. Used for
//  * the actual signing of a transaction.
//  * @param signers
//  */
//case class Signers(signers: List[Signer])
//
//object Signers {
//
//  /**
//    * The encoder for this is a List of Signers, the top level is not an object.
//    * Inside each signer json object is a field for signer. This was a bit
//    * confusing and forget so better to document with a test case or too.
//    */
//  given Encoder[Signers] = Encoder.instance[Signers] { v =>
//    val objList: immutable.Seq[JsonObject] = v.signers.map((s: Signer) => JsonObject.singleton("Signer", s.asJson))
//    objList.asJson
//  }
//
//  private case class Dummy(Signer: Signer)
//  // noinspection ScalaUnusedSymbol
//  private object Dummy { implicit val decoder: Decoder[Dummy] = deriveDecoder[Dummy] }
//
//  implicit val decoder: Decoder[Signers] = {
//    val sub: Decoder[List[Signer]] = Decoder.decodeList[Dummy].map(list => list.map(dummy => dummy.Signer))
//
//    sub.map(Signers(_))
//
//  }
//}
//
///**
//  * This is used in the SignerListSet to add signers/quorom to an account.
//  * @param account
//  * @param signerWeight
//  */
//case class SignerEntry(account: AccountAddr, signerWeight: Int)
//
///**
//  * The correct encoding and decoding within nested JsonObject is handled here
//  */
//object SignerEntry {
//
//  implicit val decode: Decoder[SignerEntry] = {
//    Decoder.forProduct2("Account", "SignerWeight")(SignerEntry.apply).prepare(_.downField("SignerEntry"))
//  }
//
//  implicit val encoder: Encoder[SignerEntry] = {
//    val base: Encoder.AsObject[SignerEntry] =
//      Encoder.forProduct2("Account", "SignerWeight")(v => (v.account, v.signerWeight))
//    base.mapJsonObject(base => JsonObject.singleton("SignerEntry", base.asJson))
//  }
//
//}
