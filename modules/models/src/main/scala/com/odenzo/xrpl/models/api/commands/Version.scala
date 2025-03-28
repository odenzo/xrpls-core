package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.Command.VERSION
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpAdminCommandRq, XrpCommand, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{ LedgerIndex, given_Encoder_LedgerHandle }
import io.circe.*
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.syntax.*

/**
  * Use this to advance and validate the current ledger only on a stand-alone
  * server. [[https://ripple.com/build/rippled-apis/#ledger-accept]].
  *
  * This is purely developer mode, not sure how to integrate in with engine or
  * higher level devkit, seperate command, flag, or a seperate engine pipeline
  * (best I think).
  *
  * @todo
  *   Testing requires a manual rippled server
  * @param id
  */
object Version extends XrpCommand[Version.Rq, Version.Rs] {

  def command: Command = Command.VERSION

  case class Rq() extends XrpAdminCommandRq derives Codec.AsObject {
    override def command: Command = VERSION
  }

  case class Rs(version:VersionInfo) extends XrpCommandRs derives Codec.AsObject

  case class VersionInfo(first: String, last: String, good: String) derives Codec.AsObject
}
