package com.odenzo.xrpl.models.api.commands.transaction

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{XrpCommand, XrpCommandRq, XrpCommandRs}
import com.odenzo.xrpl.models.data.models.atoms.LedgerHash
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import io.circe.*
import io.circe.derivation.{Configuration, ConfiguredCodec}

/**
  * Useful for checking the status of a submitted Transaction to see if its
  * validated or what.
  */
object Tx extends XrpCommand[Tx.Rq, Tx.Rs] {

  /** Note: TxnHash xor TCID required */
  case class Rq(
      transaction: Option[TxnHash],
      ctid: Option[String]           = None,
      binary: Boolean                = false,
      minLedger: Option[LedgerIndex] = None,
      maxLedger: Option[LedgerIndex] = None,
      apiVersion: Long               = 2,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.TX
  }

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames

    def forTransaction(txn: TxnHash): Rq = Rq(Some(txn), binary = false)
  }

  /** Hash is TxnHash or LedgerHash? */
  case class Rs(
      txJson: JsonObject,
      ctid: String,
      hash: TxnHash,
      meta: JsonObject,
      ledgerIndex: LedgerIndex,
      ledgerHash: LedgerHash,
      closeTimeIso: String, // In Unix TS format
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  // Add some Pointers to internal RippleDate and Sequence (Account Txn Sequence)

}
