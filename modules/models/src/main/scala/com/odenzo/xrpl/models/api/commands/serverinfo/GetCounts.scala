package com.odenzo.xrpl.models.api.commands.serverinfo





//package com.odenzo.xrpl.apis.commands.serverinfo
//
//
//import io.circe.*
//import io.circe.generic.semiauto.deriveEncoder
//import io.circe.syntax.*
//
///**
//  * https://ripple.com/build/rippled-apis/#get-counts
//  *
//  * @param min_count
//  *   Filters fields with counts less than this value
//  * @param id
//  */
//case class GetCountsRq(min_count: Long = 0) extends RippleAdminRq
//
//case class GetCountsRs(counts: Json) extends RippleAdminRs
//
//object GetCountsRq {
//  given circe: Encoder.AsObject[GetCountsRq] = CirceCodecUtils.deriveRqEncoder("get_counts")
//}
//
//object GetCountsRs {
//
//  /**
//    * This decoder is used on the result field, but has an arbitrary content.
//    * So, actually we just use the result object as Json stored in counts field
//    */
//  given Decoder[GetCountsRs] = Decoder.instance[GetCountsRs] { hc =>
//    Right(GetCountsRs(hc.value))
//  }
//}
