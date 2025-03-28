package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import io.circe.*

object ConsensusInfo extends XrpCommand[ConsensusInfo.Rq, ConsensusInfo.Rs] {

  /**
    * https://ripple.com/build/rippled-apis/#consensus-info
    *
    * No decoding of result. Docs note subject to change, so just extract as
    * needed.
    */
  case class Rq() extends XrpCommandRq derives Codec.AsObject {
    val command = Command.CONSENSUS_INFO
  }

  case class Rs(info: Json) extends XrpCommandRs derives Codec.AsObject

}
