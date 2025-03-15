package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import io.circe.Codec

/** https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/utility-methods/ping */
object Ping extends XrpCommand[Ping.Rq, Ping.Rs] {

  case class Rq() extends XrpCommandRq derives Codec.AsObject:
    val command: Command = Command.PING

  /** A Blank Result aside from std fields */
  case class Rs() extends XrpCommandRs derives Codec.AsObject

}
