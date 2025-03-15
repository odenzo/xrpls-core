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
class AccountChannelsTest extends LocalCommsTest(TestScenarios.mode) {

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

}
