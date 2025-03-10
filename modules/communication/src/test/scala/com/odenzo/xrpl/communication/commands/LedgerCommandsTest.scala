package com.odenzo.xrpl.communication.commands

import com.tersesystems.blindsight.LoggerFactory
import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.communication.rpc.engine.RPCEngine
import com.odenzo.xrpl.models.api.commands.accountinfo.AccountInfo
import com.odenzo.xrpl.models.api.commands.admin.LedgerAccept
import com.odenzo.xrpl.models.api.commands.ledgerinfo.{Ledger, LedgerClosed, LedgerCurrent, LedgerData, LedgerEntry}

import com.odenzo.xrpl.models.data.models.constants.XrpConstants
import com.odenzo.xrpl.models.data.models.keys.KeyType.secp256k1
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{ LedgerIndex, validated }
import io.circe.*
import io.circe.syntax.*
import munit.{ AnyFixture, CatsEffectFunFixtures, given }

class LedgerCommandsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("ledger") {
    given engine: XrplEngine = engineFixture()

    val T        = Ledger
    val rq       = Ledger.Rq(
      ledgerIndex  = LedgerHandle.validated,
      transactions = true,
      accounts     = true,
      expand       = true,
    )
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

  test("ledger_closed") {
    given engine: XrplEngine = engineFixture()
    val T                    = LedgerClosed
    val rq                   = LedgerClosed.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)
    response
  }

  test("ledger_current") {
    val T = LedgerCurrent

    given engine: XrplEngine = engineFixture()
    val rq                   = LedgerCurrent.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)

    response

  }

  test("ledger_data") {
    val T = LedgerData

    given engine: XrplEngine = engineFixture()

    val rq       = T.Rq(LedgerHandle.validated, binary = false)
    val response = engine.send[T.Rq, T.Rs](rq)

    response

  }

  test("ledger_entry".fail) {
    val T                    = LedgerEntry
    given engine: XrplEngine = engineFixture()
    val rq                   = T.Rq(binary = false)
    val response             = engine.send[T.Rq, T.Rs](rq)
    response

  }
}
