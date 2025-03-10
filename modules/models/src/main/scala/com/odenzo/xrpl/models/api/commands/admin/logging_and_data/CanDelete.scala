package com.odenzo.xrpl.models.api.commands.admin.logging_and_data







//package com.odenzo.xrpl.apis.commands.serverinfo
//
//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//import io.circe.syntax.*
//
//object CanDelete {
//
//  /**
//    * https://ripple.com/build/rippled-apis/#can-delete This just takes
//    * ledgerindex now. For "never" use LedgerIndex.MIN (LedgerIndex(0)) For
//    * "always" use LedgerIndex.MAX LedgerIndex(4294967295) This will give an
//    * error if online_delete is not enabled on the server (which it isn't for my
//    * testnets :-()
//    *
//    * @param can_delete
//    * @param id
//    */
//  case class CanDeleteRq(can_delete: LedgerIndex = LedgerIndex.MIN) extends RippleAdminRq
//
//  case class CanDeleteRs(can_delete: LedgerIndex) extends RippleAdminRs
//
//  object CanDeleteRq {
//
//    given Encoder.AsObject[CanDeleteRq] = CirceCodecUtils.deriveRqEncoder("can_delete")
//  }
//
//  object CanDeleteRs {
//    given Decoder[CanDeleteRs] = deriveDecoder[CanDeleteRs]
//  }
//
//}
