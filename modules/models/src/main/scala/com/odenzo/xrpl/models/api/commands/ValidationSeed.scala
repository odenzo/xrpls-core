package com.odenzo.xrpl.models.api.commands









//package com.odenzo.xrpl.apis.commands.serverinfo
//
//import cats.implicits.*
//
//import com.odenzo.ripple.models.support.{ RippleAdminRq, RippleAdminRs }
//import io.circe.*
//import io.circe.Decoder.Result
//import io.circe.generic.semiauto.deriveEncoder
//import io.circe.syntax.*
//
//object ValidationSeed {
//
//  /**
//    * @param secret
//    *   Validation secret in base58, RFC-1751, or as a passphrase format
//    *   technically. None to stop proposing validations. NO default to avoid
//    *   cutting off your toes. Noy
//    * @param id
//    */
//  case class ValidationSeedRq(secret: Option[RippleSignature] = None, id: RippleMsgId = RippleMsgId.random)
//      extends RippleAdminRq
//
//  case class ValidationSeedRs(key: RippleKey, pubKey: RipplePublicKey, seed: RippleSeed) extends RippleAdminRs
//
//  object ValidationSeedRq {
//    given circe: Encoder.AsObject[ValidationSeedRq] = CirceCodecUtils.deriveRqEncoder("validation_seed")
//  }
//
//  object ValidationSeedRs {
//    // TODO: Use renaming instead
//    given Decoder[ValidationSeedRs] = Decoder.instance[ValidationSeedRs] { cursor =>
//      val res: Result[ValidationSeedRs] =
//        (
//          cursor.get[RippleKey]("validation_key"),
//          cursor.get[RipplePublicKey]("validation_public_key"),
//          cursor.get[RippleSeed]("validation_seed"),
//        ).mapN(ValidationSeedRs(_, _, _))
//      res
//    }
//  }
//}
