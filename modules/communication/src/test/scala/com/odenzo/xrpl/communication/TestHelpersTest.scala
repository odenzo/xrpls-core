package com.odenzo.xrpl.communication

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.transactions.support.TxCommon
import com.odenzo.xrpl.models.api.transactions.{ OfferCancelTx, OfferCreateTx }
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.atoms.{ AccountTxnNumber, RippleTime }
import com.odenzo.xrpl.models.data.models.monetary.*
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

class TestHelpersTest extends LocalCommsTest(TestScenarios.mode) {
  private val log = LoggerFactory.getLogger

  test("Test amendment enablement") {
    given engine: XrplEngine = engineFixture()
    TestHelpers.enableAllAmendments
  }

}
