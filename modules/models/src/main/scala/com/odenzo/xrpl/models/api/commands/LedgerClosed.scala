package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object LedgerClosed extends XrpCommand[LedgerClosed.Rq, LedgerClosed.Rs] {

  

  /**
    * Gets the id of the last closed ledger.
    * [[https://ripple.com/build/rippled-apis/#ledger-closed]]
    */
  case class Rq() extends XrpCommandRq derives Codec.AsObject {
    val command: Command = Command.LEDGER_CLOSED
  }

  case class Rs(ledgerHash: LedgerHash, ledgerIndex: LedgerIndex) extends XrpCommandRs derives ConfiguredCodec

}
