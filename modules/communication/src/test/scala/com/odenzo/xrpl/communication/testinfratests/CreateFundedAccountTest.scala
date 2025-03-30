package com.odenzo.xrpl.communication.testinfratests

import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.keys.KeyType
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.*
import com.odenzo.xrpl.models.internal.Wallet
import com.tersesystems.blindsight.LoggerFactory

class CreateFundedAccountTest extends LocalCommsTest("rpc") {
  private val log = LoggerFactory.getLogger

  test("Created New Funded Account") {
    given engine: XrplEngine = engineFixture()
    log.info(s"Created  Funded Account Testing with Engine ${engine.engineName}")
    for {
      wallet <- TestScenarios.createFundedAccount(amount = CurrencyAmount.xrp(5555), fundedBy = Wallet.GENESIS)
      _      <- engine.ledgerAccept
    } yield wallet
    end for

  }

  test("TestKit QuickAccountOpen ") {
    given engine: XrplEngine = engineFixture()

    for {
      wallet  <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(5555), Wallet.GENESIS)
      _       <- engine.ledgerAccept
      balance <- TestHelpers.checkXrpAccountBalance(wallet.accountAddress, validated)
      _        = log.info(s"XRP Balance of new account  is ${balance.asXrp}")
    } yield ()

  }

  test("TestKit Create SecP Account") {
    given engine: XrplEngine = engineFixture()

    for {
      result <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(10L), Wallet.GENESIS, KeyType.secp256k1)
      _       = log.debug(s"Result: $result")
    } yield result

  }

  test("TestKit Create ED Account") {
    given engine: XrplEngine = engineFixture()

    for {
      result <-
        TestScenarios.createFundedAccount(CurrencyAmount.xrp(55L), Wallet.GENESIS, KeyType.ed25519)
      _       = log.debug(s"Result: $result")
    } yield result

  }

  test("validation_create") {
    val T: ValidationCreate.type = ValidationCreate

    given engine: XrplEngine = engineFixture()

    val response = engine.send[T.Rq, T.Rs](T.Rq(None))
    response

  }

}
