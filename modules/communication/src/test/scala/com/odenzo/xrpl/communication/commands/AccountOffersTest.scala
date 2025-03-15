package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.*
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
class AccountOffersTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

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

}
