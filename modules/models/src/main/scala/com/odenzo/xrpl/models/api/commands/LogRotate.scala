package com.odenzo.xrpl.models.api.commands

object LogRotate {}

//package com.odenzo.xrpl.apis.commands.serverinfo
//
//import com.odenzo.ripple.models.support.{ RippleAdminRq, RippleRs }
//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//import io.circe.syntax.*
//
///**
//  * https://ripple.com/build/rippled-apis/#logrotate
//  *
//  * @param id
//  */
//case class LogRotateRq() extends RippleAdminRq
//
//case class LogRotateRs(message: String) extends RippleRs
//
//object LogRotateRq {
//
//  given circe: Encoder.AsObject[LogRotateRq] = CirceCodecUtils.deriveRqEncoder("logrotate")
//}
//
//object LogRotateRs {
//  given Decoder[LogRotateRs] = deriveDecoder[LogRotateRs]
//}
