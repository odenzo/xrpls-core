package com.odenzo.xrpl.models.api.commands.serverinfo

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.syntax.*

/**
  * Get the current XRPL server Fee statistics.
  * https://ripple.com/build/rippled-apis/#server-info
  */
object Manifest extends XrpCommand[Manifest.Rq, Manifest.Rs] {
  import XrpPublicKey.Codecs.given

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
