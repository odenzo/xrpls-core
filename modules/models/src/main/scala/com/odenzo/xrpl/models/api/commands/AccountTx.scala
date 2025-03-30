package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs, XrpScrolling }
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.*

/**
  * https://ripple.com/build/rippled-apis/#account-tx
  *
  * Note that ledger_index_min / ledger_index_max OR ledger can be used. TODO:
  * Need to test what happens when we use both and document or enforce
  */
object AccountTx extends XrpCommand[AccountTx.Rq, AccountTx.Rs] with XrpScrolling {

  

  /**
    * You must use at least one of the following fields in your request:
    * ledger_index, ledger_hash, ledger_index_min, or ledger_index_max. Defaults
    * to all ledgers min-max wildcards
    * @param account
    * @param ledger_index_min
    * @param ledger_index_max
    * @param ledgerIndex
    * @param ledgerHash
    * @param binary
    *   false = JSON, true = Hex
    * @param forward
    *   Order the pages of txns, not ordering within a page enforced. true is
    *   ASCENDING by time.
    * @param limit
    * @param marker
    */
  case class Rq(
      account: AccountAddress,
      ledgerIndex: Option[LedgerHandle]   = None,
      ledgerHash: Option[LedgerHash]      = None,
      ledgerIndexMax: Option[LedgerIndex] = Some(LedgerHandle.WILDCARD_LEDGER),
      ledgerIndexMin: Option[LedgerIndex] = Some(LedgerHandle.WILDCARD_LEDGER),
      binary: Boolean                     = false,
      forward: Boolean                    = false,
  ) extends XrpCommandRq with XrpScrolling derives ConfiguredCodec {
    val command: Command = Command.ACCOUNT_TX
  }


  /**
    * Get List of Validated Transacations for an Accont
    *   - https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/account-methods/account_tx
    *
    * This supportd standard Pagination Represents the result= field in
    * AccountTx response. Now in Pagination which is optional. Needs to be
    * optional on response but not request?
    */
  case class Rs(
      account: AccountAddress,
      ledgerIndexMin: LedgerIndex, // Our fanciness looking for ledger_index or ledger_current_index screws this up
      ledgerIndexMax: LedgerIndex,
      offset: Option[Long], // Not in request and never seen?
      transactions: List[JsonObject], // TODO: TransactionRecords is a beast,  and TxNode
  ) extends XrpCommandRs derives ConfiguredCodec


}
