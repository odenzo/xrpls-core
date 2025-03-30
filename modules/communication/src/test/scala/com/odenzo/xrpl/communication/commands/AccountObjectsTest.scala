package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.xrp
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class AccountObjectsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

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

}
