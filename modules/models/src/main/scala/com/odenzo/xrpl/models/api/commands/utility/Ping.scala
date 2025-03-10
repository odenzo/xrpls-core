package com.odenzo.xrpl.models.api.commands.utility

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{XrpCommand, XrpCommandRq, XrpCommandRs}
import io.circe.Codec

//package com.odenzo.xrpl.apis.commands.conviencefunctions
//
//import io.circe.*
//import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
//import io.circe.syntax.*
//
///**
//  * The documentation on rippled site is incorrect. This is correct.
//  * https://ripple.com/build/rippled-apis/#ping
//  *
//  * @param id
//  */
object Ping extends XrpCommand[Ping.Rq, Ping.Rs] {

  case class Rq() extends XrpCommandRq derives Codec.AsObject:
    val command:Command = Command.PING

  //
  /// **
  //  * There in just a blank JSON object here
  //  * @param role
  //  *   I guess will be admin or somethign else.
  //  */
  case class Rs(role: String) extends XrpCommandRs derives Codec.AsObject

}
