package com.odenzo.xrpl.communication.commands

import com.tersesystems.blindsight.LoggerFactory
import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.{*, given}
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.rpc.engine.RPCEngine
import com.odenzo.xrpl.communication.{LocalCommsTest, TestScenarios, XrplEngine}
import com.odenzo.xrpl.models.api.commands.admin.LedgerAccept
import com.odenzo.xrpl.models.api.commands.admin.keygen.{ValidationCreate, WalletPropose}
import com.odenzo.xrpl.models.data.models.keys.KeyType.secp256k1
import com.odenzo.xrpl.models.data.models.keys.XrpSeed
import com.odenzo.xrpl.models.internal.Wallet.decodeStringUnsafe
import io.circe.*
import io.circe.syntax.*
import munit.{AnyFixture, CatsEffectFunFixtures, given}

class AdminCommandsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger
  test("GenesisWalletPropose") {
    given engine: XrplEngine           = engineFixture()
    val seed                           = "snoPBrXtMeMyMHUVTgbuqAfg1SUTb"
    val rq: WalletPropose.Rq           = WalletPropose.Rq(seed = XrpSeed.fromBase58Unsafe(seed).some, None, secp256k1)
    val response: IO[WalletPropose.Rs] = engine.send[WalletPropose.Rq, WalletPropose.Rs](rq).map(_.rs)
    response

  }

  test("WalletPropose") {
    given engine: XrplEngine = engineFixture()
    val rq                   = WalletPropose.Rq(seed = None, passphrase = "testing".some, secp256k1)
    log.info(rq.asJson.spaces4)
    val response             = engine.send[WalletPropose.Rq, WalletPropose.Rs](rq).map(_.rs)
    response

  }

  test("ledger_accept") {
    given engine: XrplEngine = engineFixture()
    val rq: LedgerAccept.Rq  = LedgerAccept.Rq()
    log.info(rq.asJson.spaces4)
    val response             = engine.send[LedgerAccept.Rq, LedgerAccept.Rs](rq)
    response
  }

  test("validation_create") {
    val T: ValidationCreate.type = ValidationCreate

    given engine: XrplEngine = engineFixture()
    val response             = engine.send[T.Rq, T.Rs](T.Rq(None))
    response

  }

}
