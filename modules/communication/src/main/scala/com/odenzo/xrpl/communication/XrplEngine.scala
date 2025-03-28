package com.odenzo.xrpl.communication

import cats.effect.IO
import com.odenzo.xrpl.communication.models.{ XrplEngineCommandResult, XrplEngineTxnResult }
import com.odenzo.xrpl.communication.rpc.RPCEngine
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.api.transactions.support.{ TxCommon, XrpTxn }
import com.odenzo.xrpl.models.internal.Wallet
import com.tersesystems.blindsight.Condition
import io.circe.{ Decoder, Encoder }

trait XrplEngine { engine =>

  /**
    * This sends a command that is immmediately executed and returns the result.
    * An exception is thrown on errors, typical XrplEngineCommandError
    */
  def send[RQ <: XrpCommandRq: Encoder.AsObject: Decoder, RS <: XrpCommandRs: Encoder.AsObject: Decoder](
      rq: RQ
  )(using debug: Condition = Condition.never): IO[XrplEngineCommandResult[RS]]

  /**
    * This signs and submits a Transactions to the engine, returning
    * XrplEngineTxnResult. This result is based on the submission, and the
    * actual Txn has been preliminarly accepted by the XRPL ledger. It still has
    * to undergo ledger validation, this just puts in the current ledger.
    */
  def sendTxn[T <: XrpTxn: Encoder.AsObject: Decoder](
      commonTx: TxCommon,
      txn: T,
      wallet: Wallet,
  )(using debug: Condition = Condition.never): IO[XrplEngineTxnResult]

  def ledgerAccept: IO[XrplEngineCommandResult[LedgerAccept.Rs]]

  def engineName: String = if engine.isInstanceOf[RPCEngine] then "RPCEngine" else "WSEngine Engine"
}
