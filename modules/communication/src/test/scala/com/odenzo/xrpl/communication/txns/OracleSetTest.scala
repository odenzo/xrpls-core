package com.odenzo.xrpl.communication.txns

import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.commands.NoRippleCheck.Role.{ gateway, user }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.xrp
import com.tersesystems.blindsight.LoggerFactory

/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class OracleSetTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("NoRippleCheck with Transactions") {
    given engine: XrplEngine = engineFixture()

    val T        = NoRippleCheck
    val rq       = T.Rq(AccountAddress.GENESIS, user, true)
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

  test("New Gateway Account NoRipple Check No Transactions") {
    given engine: XrplEngine = engineFixture()
    for {
      walletRs <- TestScenarios.createFundedAccount(xrp(1000))
      rq        = NoRippleCheck.Rq(walletRs.accountAddress, gateway, false)
      result   <- engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](rq)
    } yield result
  }
  test("New Gateway Account NoRipple Check With Transactions") {
    given engine: XrplEngine = engineFixture()

    for {
      walletRs <- TestScenarios.createFundedAccount(xrp(1000))
      rq        = NoRippleCheck.Rq(walletRs.accountAddress, gateway, true)
      result   <- engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](rq)
    } yield result
  }

}
