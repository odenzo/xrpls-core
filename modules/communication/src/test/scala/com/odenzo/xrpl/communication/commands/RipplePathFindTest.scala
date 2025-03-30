package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.commands.NoRippleCheck.Role.{ gateway, user }
import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.{ Drops, xrp }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

/**
  * Rough tests manually inspected. Need to warning and error cases for that
  * path, on just one command at least.
  */
class RipplePathFindTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  test("Basic Check".ignore) {
    given engine: XrplEngine = engineFixture()
    // Need to setup a path scenario

    val T        = RipplePathFind
    val rq       = T.Rq(
      ledgerIndex        = LedgerHandle.validated.some,
      sourceAccount      = AccountAddress.GENESIS,
      sourceCurrencies   = None,
      destinationAccount = AccountAddress.GENESIS,
      destinationAmount  = Drops(1233),
    )
    val response = engine.send[T.Rq, T.Rs](rq)
    response
  }

}
