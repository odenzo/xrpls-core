package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.atoms.LedgerHash
import com.odenzo.xrpl.models.data.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.atoms.hash256.Hash256
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object LedgerRequest extends XrpCommand[LedgerRequest.Rq, LedgerRequest.Rs] {
  

  /**
    * TODO: Not done at all. Asks Rippled to fetch the ledger if missing.
    * https://ripple.com/build/rippled-apis/#ledger-request This has really
    * strange response stuff (signals error if no ledger but rippled will try
    * and fetch etc). But that is signaled in the result (like a txn error)
    * WARNING: I don't use this, so its just a framework.
    *
    * @param ledger
    *   This must be a LedgerHash or LedgerIndex, no named ledgers it seems
    */
  case class Rq(ledgerIndex: LedgerIndex, ledgerHash: LedgerHash) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.LEDGER_REQUEST
  }

  case class Rs(json: Json) extends XrpCommandRs derives ConfiguredCodec

  case class LedgerRequestFailed(acquiring: Option[Json])

  case class LedgerRequestPending(
      hash: Option[LedgerHash],
      have_header: Boolean,
      have_state: Option[Boolean],
      have_transaction: Boolean,
      needed_state_hashes: List[Hash256],
      needed_transaction_hashes: List[TxnHash],
      peers: Long,
      timeouts: Long,
  )

  /**
    * Will actually give ledger information. This may be a duplicate of ledger
    * call, check. This is a Ledger Header
    * https://ripple.com/build/ledger-format/#header-format
    */
  case class LedgerRequestSucceed(ledger: Json, ledgerIndex: LedgerIndex)

}
