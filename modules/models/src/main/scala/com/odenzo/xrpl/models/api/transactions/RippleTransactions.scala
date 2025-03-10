package com.odenzo.xrpl.models.api.transactions





//package com.odenzo.xrpl.apis.transactions
//
//import io.circe.syntax.*
//import io.circe.{ Decoder, Encoder }
//
///**
//  * Ripple transactions are signed and submitted based on a tx_json object
//  * containing the transaction details. These are encoded in
//  * [TransactionName]TxRq case classes. All of which extend this trait. These
//  * are encoded for a SignRq and the tx_json of SignRs has with SigningPubKey,
//  * TxnSignature, hash... added. Other fields auto-filled (e.g. Fee, Sequence)
//  * The SubmitRs has the SignRq tx_json in its response supplemented as in the
//  * SignRs The TxRs (used to check transaction validation status) has in its
//  * `result` field the same as tx_json in submit supplemented with inLedger,
//  * ledger_index (dup), date, validated=true (if so). And the meta which is
//  * different altogether.
//  */
//trait XRPLTx {
//
//  /**
//    * Since all transactions share some common optional things, I put these into
//    * a a base class that is aggregated since case classes cannot extend other
//    * case classes in Scala
//    */
//  def base: CommonTx
//
//}
//
//object XRPLTx {
//
//  implicit val decoder: Decoder[XRPLTx] = Decoder.instance[XRPLTx] { c =>
//    c.get[RippleTxnType]("TransactionType").flatMap {
//      case AccountSet           => c.as[AccountSetTx]
//      case Payment              => c.as[PaymentTx]
//      case EscrowFinish         => c.as[EscrowFinishTx]
//      case EscrowCreate         => c.as[EscrowCreateTx]
//      case EscrowCancel         => c.as[EscrowCancelTx]
//      case TrustSet             => c.as[TrustSetTx]
//      case OfferCreate          => c.as[OfferCreateTx]
//      case OfferCancel          => c.as[OfferCancelTx]
//      case SetRegularKey        => c.as[SetRegularKeyTx]
//      case PaymentChannelCreate => c.as[PaymentChannelCreateTx]
//      case PaymentChannelFund   => c.as[PaymentChannelFundTx]
//      case PaymentChannelClaim  => c.as[PaymentChannelClaimTx]
//      case SignerListSet        => c.as[SignerListSetTx]
//    }
//  }
//
//  implicit val encoder: Encoder[XRPLTx] = Encoder.instance[XRPLTx] {
//    case tx: AccountSetTx           => tx.asJson
//    case tx: PaymentTx              => tx.asJson
//    case tx: TrustSetTx             => tx.asJson
//    case tx: EscrowCreateTx         => tx.asJson
//    case tx: EscrowCancelTx         => tx.asJson
//    case tx: EscrowFinishTx         => tx.asJson
//    case tx: SetRegularKeyTx        => tx.asJson
//    case tx: OfferCreateTx          => tx.asJson
//    case tx: OfferCancelTx          => tx.asJson
//    case tx: PaymentChannelCreateTx => tx.asJson
//    case tx: PaymentChannelFundTx   => tx.asJson
//    case tx: PaymentChannelClaimTx  => tx.asJson
//    case tx: SignerListSetTx        => tx.asJson
//  }
//
//}
//
///**
//  * This corresponds to standard fields in a transaction that has been signed OR
//  * submitted, but not yet validated.
//  * @param signingPubKey
//  * @param txnSignature
//  *   Optional so we can deal with multi-sig result too.
//  * @param hash
//  * @param txnType
//  */
//case class PendingTxData(
//    signingPubKey: SigningPublicKey,
//    txnSignature: Option[TxnSignature],
//    hash: TxnHash,
//    txnType: RippleTxnType,
//)
//
//object PendingTxData {
//
//  given Decoder[PendingTxData] =
//    Decoder.forProduct4("SigningPubKey", "TxnSignature", "hash", "TransactionType")(PendingTxData.apply)
//
//}
//
///**
//  * This is EXTRA data (on top of PendingTxData) I believe this is here is
//  * closed or validated transactions. Not positive about closed
//  */
//case class AcceptedTxData(ledgerIndex: LedgerIndex, date: RippleTime)
//
//object AcceptedTxData {
//
//  given Decoder[AcceptedTxData] = Decoder.forProduct2("ledger_index", "date")(AcceptedTxData.apply)
//
//}
