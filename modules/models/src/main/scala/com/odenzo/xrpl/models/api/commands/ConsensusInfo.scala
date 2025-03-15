package com.odenzo.xrpl.models.api.commands







//package com.odenzo.xrpl.apis.commands.serverinfo
//
//import com.odenzo.ripple.models.support.{ RippleAdminRq, RippleAdminRs }
//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//import io.circe.syntax.*
//
//object ConsensusInfo {
//
//  /**
//    * https://ripple.com/build/rippled-apis/#consensus-info
//    *
//    * No decoding of result. Docs note subject to change, so just extract as
//    * needed.
//    *
//    * @param id
//    */
//  case class ConsensusInfoRq(id: RippleMsgId = RippleMsgId.random) extends RippleAdminRq
//
//  case class ConsensusInfoRs(info: Json) extends RippleAdminRs
//
//  object ConsensusInfoRq {
//    given Encoder.AsObject[ConsensusInfoRq] = CirceCodecUtils.deriveRqEncoder("consensus_info")
//  }
//
//  object ConsensusInfoRs {
//    given Decoder[ConsensusInfoRs] = deriveDecoder[ConsensusInfoRs]
//  }
//
//}
