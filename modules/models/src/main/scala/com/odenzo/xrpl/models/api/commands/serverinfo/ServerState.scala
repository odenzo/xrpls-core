package com.odenzo.xrpl.models.api.commands.serverinfo

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import ServerState.{ Rq, Rs }
import com.odenzo.xrpl.models.api.commands.Command
import io.circe.{ Codec, Json }

//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//import io.circe.syntax.*
//
/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/server-info-methods/server_state
  *   - TODO: Medium priority decoding of ServerState Response
  */
object ServerState extends XrpCommand[Rq, Rs]:
  val command: Command = Command.SERVER_STATE

  case class Rq() extends XrpCommandRq derives Codec.AsObject {
    def command: Command = ServerState.command
  }
  case class Rs(state: Json) extends XrpCommandRs derives Codec.AsObject
