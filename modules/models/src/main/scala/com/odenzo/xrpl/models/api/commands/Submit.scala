package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.{ AccountTxnNumber, TxBlob }
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Another one with different format for WebSocket vs RPC. Uses the param
  * method for RPC
  */
object Submit extends XrpCommand[Submit.Rq, Submit.Rs] {
  val command: Command = Command.SUBMIT

  /** Rough Cut. Need to decide if its XRP Payment or not? */
  case class Rq(txBlob: TxBlob, failHard: Boolean = true) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.SUBMIT
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * TODO: We can apply the known Transaction.Rs to txJson or Generic BinCodec
    * to TxBlob
    */
  case class Rs(
                 accepted: Boolean,
                 accountSequenceAvailable: AccountTxnNumber,
                 accountSequenceNext: AccountTxnNumber,
                 applied: Boolean,
                 broadcast: Boolean,
                 engineResult: String, // This is enumerated somewhere, but when not worth it.
                 engineResultCode: Long,
                 engineResultMessage: String,
                 kept: Boolean,
                 openLedgerCost: Drops,
                 queued: Boolean,
                 txBlob: TxBlob,
                 txJson: JsonObject,
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
