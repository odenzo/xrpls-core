package com.odenzo.xrpl.models.api.commands.admin.logging_and_data

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object LogLevel extends XrpCommand[LogLevel.Rq, LogLevel.Rs] {
//
  /**
    * [[https://ripple.com/build/rippled-apis/#log-level]] Docs on ripple need
    * correcting too.
    *
    * @param id
    */
  case class Rq(
      severity: Option[RippleLogLevel] = None,
      partition: RippleLogPartitions   = RippleLogPartitions.BASE,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.LOG_LEVEL
  }

  /** @param levels Only there if you don't set a severity level it seems. */
  case class Rs(levels: Option[List[RippleLogLevel]]) extends XrpCommandRs derives ConfiguredCodec

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

}
