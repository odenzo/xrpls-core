package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Get the current XRPL server Fee statistics.
  * https://ripple.com/build/rippled-apis/#server-info
  */
object Manifest extends XrpCommand[Manifest.Rq, Manifest.Rs] {
  import XrpPublicKey.given

  /** Case Object that is empty? */
  case class Rq(publicKey: XrpPublicKey) extends XrpCommandRq derives ConfiguredCodec {
    def command: Command = Command.MANIFEST
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class ManifestDetails(domain: String, ephemeralKey: String, masterKey: String, seq: Int) derives ConfiguredCodec

  object ManifestDetails:
    protected given Configuration = Configuration.default.withSnakeCaseMemberNames

  /** To to more strongly type */
  case class Rs(
      details: Option[ManifestDetails],
      manifest: Option[String], // Base64 String
      requested: XrpPublicKey, // From the request.
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    protected given Configuration = Configuration.default.withSnakeCaseMemberNames

}
