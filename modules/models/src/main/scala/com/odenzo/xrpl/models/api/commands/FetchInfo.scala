package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import io.circe.*

/**
  * https://ripple.com/build/rippled-apis/#fetch-info This is another admin
  * command used for diagnosing RCL network really.
  */

object FetchInfo extends XrpCommand[FetchInfo.Rq, FetchInfo.Rs] {

  // I think case object will muck things up
  case class Rq() extends XrpCommandRq derives Codec.AsObject {
    val command: Command = Command.FETCH_INFO
  }

  case class Rs(info: Json) extends XrpCommandRs derives Codec.AsObject

}
