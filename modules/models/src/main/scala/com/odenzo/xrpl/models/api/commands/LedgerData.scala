package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.ledgertree.LedgerHeader
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/ledger-methods/ledger_data
  * This will probably end up being a seperate approach since its more RESTish
  * in that you can do follow on calls to elaborate. added ability to filter:
  * type allows the rpc client to specify what type of ledger entries to
  * retrieve. The available types are:
  *
  * "account" "amendments" "directory" "fee" "hashes" "offer" "signer_list"
  * "state" "ticket"
  */
object LedgerData extends XrpCommand[LedgerData.Rq, LedgerData.Rs] {

  case class Rq(
      ledgerIndex: LedgerHandle = LedgerHandle.validated,
      binary: Boolean           = false,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.LEDGER_DATA
  }
  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * TODO: Elaborate ledger is really Leader Header and is deprecated so
    * removed.
    *
    * State, List of JsonObjects, but actually all are LedgerNodes
    */
  case class Rs(
      ledger: LedgerHeader,
      state: List[JsonObject], // LedgerNode - trait covering N types of data
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
