package com.odenzo.xrpl.models.api.commands.ledgerinfo

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.LedgerHash
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * This will be a complicated one and I don't use it yet.
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/ledger-methods/ledger_entry
  * NOTOPERATIONAL YET PErhaps better to have smart factory constructors in
  * LedgerEntryRq and kitchen sink here. TODO: Same with ome other helpers that
  * are in API layer now. TODO: Future roadmap implementation of LedgerEntry
  * inquiry
  */
object LedgerEntry extends XrpCommand[LedgerEntry.Rq, LedgerEntry.Rs] {

  case class Rq(
      ledgerIndex: LedgerHandle       = LedgerHandle.validated,
      ledgerHash: Option[LedgerHash]  = None,
      binary: Boolean                 = false,
      includeDeleted: Option[Boolean] = None, // Error unless calling Clio Server
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.LEDGER_ENTRY
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(
      index: String,
      node: Option[JsonObject], // AccountRootNode?
      nodeBinary: Option[String],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
