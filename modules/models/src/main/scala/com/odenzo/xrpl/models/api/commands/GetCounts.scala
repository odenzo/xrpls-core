package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Admin command:
  * https://xrpl.org/docs/references/http-websocket-apis/admin-api-methods/status-and-debugging-methods/get_counts
  */
object GetCounts extends XrpCommand[GetCounts.Rq, GetCounts.Rs] {

  /**
    * https://ripple.com/build/rippled-apis/#get-counts
    *
    * @param min_count
    *   Filters fields with counts less than this value
    * @param id
    */
  case class Rq(minCount: Long = 0) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.GET_COUNTS
  }

  case class Rs(counts: Json) extends XrpCommandRs derives ConfiguredCodec

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }
}
