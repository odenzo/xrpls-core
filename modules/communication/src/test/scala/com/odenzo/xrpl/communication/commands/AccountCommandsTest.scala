package com.odenzo.xrpl.communication.commands

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.rpc.engine.RPCEngine
import com.odenzo.xrpl.communication.websocket.WSEngine
import com.odenzo.xrpl.communication.{LocalCommsTest, TestScenarios, XrplEngine}
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{XrpCommand, XrpCommandRq, XrpCommandRs}
import com.odenzo.xrpl.models.api.commands.accountinfo.{AccountChannels, AccountCurrencies, AccountInfo, AccountLines, AccountObjects, AccountOffers, AccountTx, GatewayBalances, NoRippleCheck}
import com.odenzo.xrpl.models.api.commands.accountinfo.NoRippleCheck.Role.{gateway, user}

import com.odenzo.xrpl.models.data.models.constants.XrpConstants
import com.odenzo.xrpl.models.data.models.keys.KeyType.secp256k1
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{current, validated}
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.{Drops, xrp}
import com.tersesystems.blindsight.LoggerFactory
import io.circe.*
import io.circe.syntax.*
import munit.catseffect.IOFixture
import munit.{AnyFixture, CatsEffectFunFixtures, given}

import scala.concurrent.duration.given
import com.odenzo.xrpl.models.data.models.atoms.{*, given}
/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class AccountCommandsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("account_channels") {
    given engine: XrplEngine = engineFixture()

    val T  = AccountChannels
    val rq =
      T.Rq(AccountAddress.GENESIS, AccountAddress.GENESIS, validated) // TODO: Need two account to check channel between
    for {
      response <- engine.send[T.Rq, T.Rs](rq)
    } yield response

  }

  test("account_currencies") {
    given engine: XrplEngine = engineFixture()
    println(s"The Engine $engine")
    val T                    = AccountCurrencies
    val rq                   = AccountCurrencies.Rq(AccountAddress.GENESIS, validated)
    for {
      response <- engine.send[T.Rq, T.Rs](rq)
    } yield response

  }

  test("account_currencies current") {
    given engine: XrplEngine = engineFixture()
    val T                    = AccountCurrencies
    val rq                   = AccountCurrencies.Rq(AccountAddress.GENESIS, LedgerHandle.current)
    for {
      response <- engine.send[T.Rq, T.Rs](rq)
    } yield response
  }

  /** TODO: Account Flags Need Testing */
  test("account_info") {
    given engine: XrplEngine = engineFixture()
    val T                    = AccountInfo
    val rq                   = T.Rq(
      account     = AccountAddress.GENESIS,
      queue       = false,
      signerLists = true,
      ledgerIndex = validated,
    )
    for {
      response <- engine.send[T.Rq, T.Rs](rq)
    } yield response

  }

  test("account_lines") {
    given engine: XrplEngine = engineFixture()
    val T                    = AccountLines
    val rq                   = AccountLines.Rq(
      account     = AccountAddress.GENESIS,
      ledgerIndex = LedgerHandle.current.some,
      ledgerHash  = None,
      peer        = Option.empty[AccountAddress],
    )
    for {
      response <- engine.send[T.Rq, T.Rs](rq)
    } yield response

  }

  test("account_objects") {
    given engine: XrplEngine = engineFixture()
    val T                    = AccountObjects
    val rq                   = AccountObjects.Rq(
      account              = AccountAddress.GENESIS,
      ledgerIndex          = LedgerHandle.validated,
      deletionBlockersOnly = false.some,
      `type`               = None, // AccountObjects.AccountObjectType.state.some,
    )
    val response             = engine.send[T.Rq, T.Rs](rq)
    response
  }

  test("ACCOUNT_OFFERS") {
    given engine: XrplEngine = engineFixture()

    val T        = AccountOffers
    val rq       = T.Rq(
      account     = AccountAddress.GENESIS,
      ledgerIndex = LedgerHandle.validated.some,
    )
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

  test("GATEWAY_BALANCES") {
    given engine: XrplEngine = engineFixture()

    val T        = GatewayBalances
    val rq       = T.Rq(
      account     = AccountAddress.GENESIS,
      ledgerIndex = LedgerHandle.validated.some,
    )
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

  test("account_tx ") {
    given engine: XrplEngine = engineFixture()

    val T        = AccountTx
    val rq       = T.Rq(
      account     = AccountAddress.GENESIS,
      ledgerIndex = LedgerHandle.validated.some,
    )
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

  test("NoRipple Check") {
    given engine: XrplEngine = engineFixture()
    println(s"The Engine $engine")
    val rq                   = NoRippleCheck.Rq(AccountAddress.GENESIS, user, true)
    println(s"Created the Request: $rq")
    for {
      response <- engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](rq)
      _        <- IO(log.info(s"Response $response"))
    } yield response

  }

  test("New Account NoRipple Check".ignore) {
    given engine: XrplEngine = engineFixture()
    println(s"The Engine $engine")
    for {
      walletRs <-
        TestScenarios.createFundedAccount(xrp(1000)) // This fails after propose wallet with no error. Broken Txn?
      rq        = NoRippleCheck.Rq(walletRs.accountAddress, gateway, true)
      _         = println(s"Request: ${rq.asJson.spaces4}")
      result   <- engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](rq)
    } yield result

  }

}
