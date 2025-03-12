package com.odenzo.xrpl.communication.txns

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.models.api.commands.orderbooks.BookOffers
import com.odenzo.xrpl.models.api.transactions.support.TxCommon
import com.odenzo.xrpl.models.api.transactions.{ OfferCancelTx, OfferCreateTx }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.monetary.*
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

/**
  * Get some trace information from Signing to enable testing of stand-alone
  * library for signing a txn. Basically, we need a txn signature
  */
class SignDataGeneratorTest extends LocalCommsTest(TestScenarios.mode) {
  private val log = LoggerFactory.getLogger

  test("Open TrustLines and Make an Offer or Two") {
    given engine: XrplEngine = engineFixture()
    val currency             = XrplStdCurrency.NZD.currency
    for {
      accounts          <- TestScenarios.createAccountsWithTrustLine(FiatValue("10000"), currency)
      _                  = log.debug("Finished Creating Accounts with Trust Line")
      (issuer, receiver) = accounts
      script             = Script(currency, issuer.accountAddress)
      // If the offerer doesn't have both sides as balance then tecUNFUNDED_OFFER
      // I guess the offer is still there and we can fund before or after.
      _                  = log.debug("About to check FiatBalances")
      receiverBalance   <- TestHelpers.checkFiatAccountBalances(receiver.accountAddress)

      _             = log.info(s"Receiver Fiat Balances: ${receiverBalance.asJson.spaces4}")
      offerRq       = OfferCreateTx(
                        account       = issuer.accountAddress,
                        expiration    = RippleTime.now.plusSeconds(30).some,
                        offerSequence = Option.empty[AccountTxnNumber],
                        takerGets     = script.amount("1.99"),
                        takerPays     = CurrencyAmount.xrp(1),
                      )
      result       <- engine.sendTxn(TxCommon.default, offerRq, issuer)
      rs            = result.submitted
      txJson        = result.submitted.txJson
      // Use a pointer into AccountTxnSequence. Probably put in OfferCreateTx unless its more common
      sequence     <- IO.fromOption(txJson("Sequence").flatMap(_.as[AccountTxnNumber].toOption))(
                        throw Throwable("Not Sequence key found")
                      )
      txnHashJ     <- IO.fromOption(txJson("hash"))(throw Throwable("Not hash key found"))
      txnHash      <- IO.fromEither(txnHashJ.as[TxnHash]) // TODO: Use a pointer or jsonpath
      _            <- engine.ledgerAccept
//      txRs         <- rpc.send[Tx.Rq, Tx.Rs](Tx.Rq.forTransaction(txnHash))
//      _             = log.info(s"TxRs: ${txRs.asJson.spaces4}")
      bookRq        = BookOffers.Rq(None, Script(XrplStdCurrency.NZD.currency, issuer.accountAddress), BookCurrency.XRP)
      bookRs       <- engine.send[BookOffers.Rq, BookOffers.Rs](bookRq)
      _             = log.info(s"BookRs: ${bookRs.rs.asJson.spaces4}")
      offerCancel   = OfferCancelTx(issuer.accountAddress, sequence)
      cancelRs     <- engine.sendTxn(TxCommon.default, offerCancel, issuer)
      _             = log.debug(s"CancelRs: ${cancelRs.submitted.asJson.spaces4}")
      _            <- engine.ledgerAccept
      afterDeleted <- engine.send[BookOffers.Rq, BookOffers.Rs](bookRq)

    } yield ()
    end for

  }

}
