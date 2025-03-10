package com.odenzo.xrpl.models.api.commands.serverinfo

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import io.circe.*
import io.circe.syntax.*

/**
  * https://ripple.com/build/rippled-apis/#server-info TODO: Do at least partial
  * decoding.
  * @param id
  */
object ServerInfo extends XrpCommand[ServerInfo.Rq, ServerInfo.Rs] {
  val command: Command = Command.SERVER_INFO
  case class Rq(counters: Boolean) extends XrpCommandRq derives Codec.AsObject {
    def command: Command = Command.SERVER_INFO
  }

  case class Rs(info: Json) extends XrpCommandRs derives Codec.AsObject

}
