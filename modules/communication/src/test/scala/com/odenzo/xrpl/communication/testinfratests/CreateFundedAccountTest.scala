package com.odenzo.xrpl.communication.testinfratests

import com.tersesystems.blindsight.LoggerFactory
import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.IO.{IOCont, Uncancelable, some}
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.XrplBase58Alphabet
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.communication.rpc.engine.RPCEngine
import com.odenzo.xrpl.models.api.commands.accountinfo.{AccountCurrencies, NoRippleCheck}
import com.odenzo.xrpl.models.api.commands.admin.keygen.ValidationCreate
import com.odenzo.xrpl.models.api.transactions.{PaymentTx, TrustSetTx}
import com.odenzo.xrpl.models.api.transactions.support.TxCommon

.*
import com.odenzo.xrpl.models.data.models.keys.KeyType.{ed25519, secp256k1}
import com.odenzo.xrpl.models.data.models.keys.{KeyType, XrpSeed}
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{LedgerIndex, current, validated}
import com.odenzo.xrpl.models.data.models.memos.{Memo, MemoBuilder}
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.*
import com.odenzo.xrpl.models.data.models.monetary.FiatValue.*
import com.odenzo.xrpl.models.data.models.monetary.{CurrencyAmount, FiatValue, Script, XrplCurrency}
import com.odenzo.xrpl.models.internal.Wallet
import io.circe.*
import io.circe.syntax.*
import io.scalaland.chimney.partial.Result
import munit.catseffect.IOFixture
import munit.{AnyFixture, CatsEffectFunFixtures, given}
import scodec.bits.{ByteVector, hex}

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

    given engine: XrplEngine = engineFixture()

    val response = engine.send[T.Rq, T.Rs](T.Rq(None))
    response

  }

}
