package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import io.circe.Codec

/**
  * The documentation on rippled site is incorrect. This is correct.
  * https://ripple.com/build/rippled-apis/#connect
  *
  * You can throw in any address and it returns connecting, so this is just
  * asking it to attempt it. Not confirming it can do it.
  *
  * @param id
  * @param ip
  *   "ip": "192.170.145.88". Haven't tried IPV6
  */
object Connect extends XrpCommand[Connect.Rq, Connect.Rs] {
  case class Rq(ip: String, port: Int) extends XrpCommandRq derives Codec.AsObject {
    val command: Command = Command.CONNECT
  }

  /** There in just a blank JSON object here */
  case class Rs(message: String) extends XrpCommandRs derives Codec.AsObject

}
