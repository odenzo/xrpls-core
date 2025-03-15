package com.odenzo.xrpl.models.api.commands.transaction

//package com.odenzo.xrpl.communication.websocket.transactionOps
//
//
//import com.odenzo.ripple.models.support.{ RippleRq, RippleRs }
//import com.odenzo.ripple.models.wireprotocol.serverinfo.ServerInfoRq
//import com.odenzo.ripple.models.wireprotocol.transactions.transactiontypes.{ PendingTxData, XRPLTx }
//import io.circe.*
//import io.circe.generic.semiauto.deriveEncoder
//
///**
//  * [[https://ripple.com/build/rippled-apis/#sign]]
//  *
//  * @param tx_json
//  *   The actual transaction data, must have SigningPubKey blank
//  * @param secret
//  *   Will automatically fill correctly.
//  * @param id
//  */
//case class SignForRq(
//    account: AccountAddr,
//    tx_json: XRPLTx,
//    secret: RippleSignature,
//    id: RippleMsgId = RippleMsgId.random,
//) extends RippleRq
//
///** Ultimately this should/could use SignRs? */
//case class SignForRs(tx_blob: TxBlob, signed: PendingTxData, tx_json: XRPLTx) extends RippleRs
//
//object SignForRq {
//  given circe: Encoder.AsObject[SignForRq] = CirceCodecUtils.deriveRqEncoder("sign_for")
//}
//
//object SignForRs {
//  // implicit val decoder: Decoder[SignRs] = deriveDecoder[SignRs]
//  // Fix root codecs and derive
//  given Decoder[SignForRs] = Decoder.instance { c =>
//    for {
//      txblob <- c.get[TxBlob]("tx_blob")
//      signed <- c.get[PendingTxData]("tx_json")
//      txrs   <- c.get[XRPLTx]("tx_json")
//
//    } yield SignForRs(txblob, signed, txrs)
//  }
//}
