package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpAdminCommandRq, XrpCommand, XrpCommandRs }
import io.circe.*

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
object LedgerAccept extends XrpCommand[LedgerAccept.Rq, LedgerAccept.Rs] {

  def command: Command = Command.LEDGER_ACCEPT

  case class Rq() extends XrpAdminCommandRq derives Codec.AsObject {
    override def command: Command = LedgerAccept.command
  }

  case class Rs(ledger_current_index: Long) extends XrpCommandRs derives Codec.AsObject

}
