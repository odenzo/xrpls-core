package com.odenzo.xrpl.models.api.commands

object LedgerCleaner {}

//package com.odenzo.xrpl.apis.commands.ledgerinfo
//
//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//import io.circe.syntax.*
//
///**
//  * https://ripple.com/build/rippled-apis/#ledger-cleaner Requests defaults to
//  * all nones. Result is just message.
//  */
//case class LedgerCleanerRq(
//    ledger: Option[LedgerIndex]     = None,
//    max_ledger: Option[LedgerIndex] = None,
//    min_ledger: Option[LedgerIndex] = None,
//    full: Option[Boolean]           = None,
//    fix_txns: Option[Boolean]       = None,
//    check_nodes: Option[Boolean]    = None,
//    stop: Option[Boolean]           = None,
//    id: RippleMsgId                 = RippleMsgId.random,
//) extends RippleAdminRq
//
//case class LedgerCleanerRs(message: String) extends RippleAdminRs
//
//object LedgerCleanerRq {
//  given Encoder.AsObject[LedgerAcceptRq] = CirceCodecUtils.deriveRqEncoder("ledger_cleaner")
//}
//
//object LedgerCleanerRs {
//  implicit val decoder: Decoder[LedgerCleanerRs] = deriveDecoder[LedgerCleanerRs]
//}
