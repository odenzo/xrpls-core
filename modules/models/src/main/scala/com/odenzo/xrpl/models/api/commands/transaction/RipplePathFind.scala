package com.odenzo.xrpl.models.api.commands.transaction

//package com.odenzo.xrpl.communication.websocket.transactionOps
//
//import com.odenzo.ripple.localops.utils.CirceCodecUtils
//import com.odenzo.ripple.models.atoms.*
//import com.odenzo.ripple.models.atoms.CurrencyAmount.FiatAmount
//import com.odenzo.ripple.models.support.{ RippleRq, RippleRs }
//import com.odenzo.ripple.models.wireprotocol.serverinfo.ServerInfoRq
//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//
///**
//  * This is the subset of the Path finding currently in use. Not full. We should
//  * probably include currencies, although we have one Issuer for each currency
//  * in current cases. https://ripple.com/build/rippled-apis/#path-find
//  * https://ripple.com/build/rippled-apis/#ripple-path-find NOTE: TODO: I will
//  * implement path_find too. Under subscriptions maybe.
//  */
//case class RipplePathFindRq(
//    source_account: AccountAddr,
//    desintation_account: AccountAddr,
//    destination_amount: FiatAmount,
//    send_max: FiatAmount,
//    paths: Option[List[PaymentPath]],
//    id: RippleMsgId = RippleMsgId.random,
//) extends RippleRq
//
//case class RipplePathFindRs(json: Json) extends RippleRs
//
//object RipplePathFindRq {
//
//  given circe: Encoder.AsObject[RipplePathFindRq] = CirceCodecUtils.deriveRqEncoder("ripple_path_find")
//}
//
//object RipplePathFindRs {
//  given Decoder[RipplePathFindRs] = deriveDecoder[RipplePathFindRs]
//}
