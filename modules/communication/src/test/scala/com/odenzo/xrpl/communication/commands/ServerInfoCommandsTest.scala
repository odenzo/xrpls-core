package com.odenzo.xrpl.communication.commands

import com.tersesystems.blindsight.LoggerFactory
import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.rpc.engine.RPCEngine
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.accountinfo.AccountInfo
import com.odenzo.xrpl.models.api.commands.admin.LedgerAccept
import com.odenzo.xrpl.models.api.commands.ledgerinfo.{LedgerClosed, LedgerCurrent}
import com.odenzo.xrpl.models.api.commands.serverinfo.{Fee, ServerDefinitions}

import com.odenzo.xrpl.models.data.models.constants.XrpConstants
import com.odenzo.xrpl.models.data.models.keys.KeyType.secp256k1
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.internal.definitions.ServerDefinitionsData
import io.circe.*
import io.circe.syntax.*
import munit.{ AnyFixture, CatsEffectFunFixtures, given }

class ServerInfoCommandsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("Fee") {
    given engine: XrplEngine = engineFixture()
    val T                    = Fee
    val rq                   = Fee.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)
    response

  }

  test("ledger_current") {
    given engine: XrplEngine = engineFixture()
    val T                    = LedgerCurrent
    val rq                   = LedgerCurrent.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq).map(io => io)
    response
  }

  test("server_definitions") {
    given engine: XrplEngine = engineFixture()
    val T                    = ServerDefinitions
    val rq                   = ServerDefinitions.Rq()
    val response             = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
