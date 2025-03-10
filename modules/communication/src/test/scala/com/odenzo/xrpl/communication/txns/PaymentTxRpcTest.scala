package com.odenzo.xrpl.communication.txns

import cats.effect.*
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.accountinfo.NoRippleCheck
import com.odenzo.xrpl.models.api.commands.accountinfo.NoRippleCheck.Role
import com.odenzo.xrpl.models.api.commands.admin.keygen.ValidationCreate
import com.odenzo.xrpl.models.api.transactions.TrustSetTx
import com.odenzo.xrpl.models.api.transactions.support.TxCommon
import com.odenzo.xrpl.models.data.models.keys.KeyType
import com.odenzo.xrpl.models.data.models.keys.KeyType.secp256k1
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.*
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, Script, XrplCurrency }
import com.odenzo.xrpl.models.internal.Wallet
import com.tersesystems.blindsight.LoggerFactory

class PaymentTxRpcTest extends LocalCommsTest("rpc") {
  private val log = LoggerFactory.getLogger

  test("Open TrustLines") {
    given engine: XrplEngine = engineFixture()
    for {
      issuerWallet       <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(5555), Wallet.GENESIS)
      receiverWallet     <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(6666), Wallet.GENESIS)
      _                  <- engine.ledgerAccept
      _                   = log.info(s"Created Two Funded Accounts")
      // Two Accounts - have the receiver open a trust line for 1000$QQQ from the issuer
      currency            = XrplCurrency.fromIsoLikeString("QQQ")
      _                   = log.info(s"Currency $currency")
      issuer              = issuerWallet.accountAddress
      issuerScript        = Script(currency, issuer)
      trustSet            = TrustSetTx(receiverWallet.accountAddress, issuerScript.amount("1000.00"))
      _                   = log.info("About to send the first Explicit Txn")
      rs                 <- engine.sendTxn(TxCommon.default, trustSet, receiverWallet)
      _                  <- engine.ledgerAccept
      _                  <- IO(log.info(s"Back from Engine Send, Checking Currencies"))
      issuerCurrencies   <- TestHelpers.listAccountCurrencies(issuerWallet.accountAddress) // This is not getting called?!
      _                   = println("Got Account Currencies")
      _                  <- IO(log.info("Got the Account Currencies"))
      receiverCurrencies <- TestHelpers.listAccountCurrencies(receiverWallet.accountAddress)
      _                   = assert(issuerCurrencies.sendCurrencies.contains(currency))
      _                   = assert(receiverCurrencies.receiveCurrencies.contains(currency))
      // Now make a transfer from issuer to receive and check the balances. Could also check the Ripple Flags
      // And see if we can send the other direction from receiver to issuer after getting some amount of money
      _                  <- TestHelpers.transferFiat(issuerWallet,
                                                     receiverWallet.accountAddress,
                                                     issuerScript.amount("222.22"),
                                                     paths = None,
                                                    )
      _                  <- engine.ledgerAccept
      _                   = println("Checking Balances")
      issuerBalances     <- TestHelpers.checkBalances(issuerWallet.accountAddress)
      receiverBalances   <- TestHelpers.checkBalances(receiverWallet.accountAddress)
      _                   = log.info(s"Issuer Balances: $issuerBalances") // Send out so negative
      _                   = log.info(s"Receiver Balances: $receiverBalances") // Positive Balance
      issuerOk           <-
        engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](NoRippleCheck.Rq(issuerWallet.accountAddress, Role.user))
      _                   = log.info(s"IssuerOk: $issuerOk")
      noRippleRq          = NoRippleCheck.Rq(receiverWallet.accountAddress, Role.user)
      receiverOk         <- engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](noRippleRq)
      _                   = log.info(s"ReceiverOk: $receiverOk")
    } yield ()
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
      result <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(10L), Wallet.GENESIS, secp256k1)
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
    given engine: XrplEngine     = engineFixture()
    val response                 = engine.send[T.Rq, T.Rs](T.Rq(None))
    response

  }

}
