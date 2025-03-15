package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.commands.NoRippleCheck.Role.{ gateway, user }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.xrp
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class NoRippleCheckTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

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
