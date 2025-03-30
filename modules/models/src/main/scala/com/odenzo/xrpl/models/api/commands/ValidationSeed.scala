package com.odenzo.xrpl.models.api.commands

import cats.implicits.*
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.keys.XrpPrivateKey.given
import com.odenzo.xrpl.models.data.keys.XrpPublicKey.given
import com.odenzo.xrpl.models.data.keys.{ XrpPrivateKey, XrpPublicKey, XrpSeed }
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
object ValidationSeed extends XrpCommand[ValidationSeed.Rq, ValidationSeed.Rs] {

  /**
    * @param secret
    *   Validation secret in base58, RFC-1751, or as a passphrase format
    *   technically. None to stop proposing validations. NO default to avoid
    *   cutting off your toes. Noy
    * @param id
    */
  case class Rq(secret: Option[XrpSeed] = None) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.VALIDATION_SEED
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(validationKey: XrpPrivateKey, validationPublicKey: XrpPublicKey, validationSeed: XrpSeed)
      extends XrpCommandRs derives ConfiguredCodec

  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }
}
