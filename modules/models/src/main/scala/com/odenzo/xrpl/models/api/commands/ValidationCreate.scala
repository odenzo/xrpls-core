package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpAdminCommandRq, XrpCommand, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.keys.{ XrpPublicKey, XrpRFC1751Passphrase, XrpSeed }
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * This sucks but is Rq and Rs in XrpCommand are wrong then the program hangs.
  * No idea why
  */
object ValidationCreate extends XrpCommand[ValidationCreate.Rq, ValidationCreate.Rs] {

  import XrpPublicKey.Codecs.given

  /**
    * [[https://ripple.com/build/rippled-apis/#validation-create]] This is an
    * admin command Creates a validation seed. Useful only for admin work.
    * Usable across servers btw.
    *
    * @param secret
    *   Secret in word or hash format FIXME: Get the Secret types sorted out.
    *   (RFC-1751 or Ripple Base 58)
    * @param secret
    *   XrpSeed, if you want to use RFC1751 or passphrase convert before using
    *   this API
    */
  case class Rq(secret: Option[XrpSeed]) extends XrpAdminCommandRq derives Codec.AsObject:
    override def command: Command = Command.VALIDATION_CREATE

  /**
    * @param validationKey
    *   RFC1751 format.
    * @param validationPublicKey
    *   Base58 format
    * @param validationSeed
    *   Says MasterSeed type but it is really just a XrplSeed and type should be
    *   renamed
    */
  case class Rs(validationKey: XrpRFC1751Passphrase, validationPublicKey: XrpPublicKey, validationSeed: XrpSeed)
      extends XrpCommandRs derives ConfiguredCodec

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

}
