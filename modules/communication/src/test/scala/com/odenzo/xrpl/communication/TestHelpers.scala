package com.odenzo.xrpl.communication

import cats.data.*
import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.models.{ ResponseLedgerInfo, XrplEngineCommandResult, XrplEngineTxnResult }
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.transactions.PaymentTx
import com.odenzo.xrpl.models.api.transactions.support.TxCommon
import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.atoms.hash256.Hash256
import com.odenzo.xrpl.models.data.keys.{ KeyType, XrpSeed }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.{ Drops, FiatAmount }
import com.odenzo.xrpl.models.data.monetary.{ CurrencyAmount, TrustLine }
import com.odenzo.xrpl.models.data.paths.PaymentPath
import com.odenzo.xrpl.models.data.{ Amendment, Vetoed }
import com.odenzo.xrpl.models.internal.Wallet
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.given

/**
  * Test Helpers - rough and ready - These use a variant of XRPLEngine,
  * accepting RPCEngine or WSEngine
  */
object TestHelpers {

  private val log = LoggerFactory.getLogger

  val txCommomDefault: TxCommon = TxCommon(fee = CurrencyAmount.Drops(555).some)

  // Create a WalletPropose Request to make an account with random passphrase. Account is not created until XRP sent and
  // validated txn.
  def proposeWallet(keyType: KeyType = KeyType.ed25519)(using engine: XrplEngine): IO[Wallet] = {
    log.debug(s"Proposing Wallet with ${engine.engineName}")
    val passphrase = Some(XrpSeed.randomPassphrase)
    val rq         = WalletPropose.Rq(seed = None, passphrase = passphrase, keyType)
    for {
      _        <- IO(log.warn("About to send"))
      response <- engine.send[WalletPropose.Rq, WalletPropose.Rs](rq)
      _        <- IO(log.info(s"WalletPropedResponse: ${response.rs.asJson.spaces2}"))
      wallet    = Wallet.fromProposedWalletUnsafe(response.rs)
    } yield wallet
  }

  /**
    * Handy thing to make new test accounts and fund them. Accept the ledger
    * automatically. This (with stand-alone) does guarantee its validated on
    * successful submission, but 99%. Need something like
    * "confirmValidationOfTxn(x: SubmitRs):IO[Failed|Succeeded]"
    */
  def fundAccount(
      proposed: Wallet,
      drops: CurrencyAmount.Drops,
      from: Wallet       = Wallet.GENESIS,
      txCommon: TxCommon = txCommomDefault,
  )(using engine: XrplEngine): IO[Submit.Rs] = {
    val paymentRq = PaymentTx(
      account     = from.accountAddress,
      amount      = drops,
      destination = proposed.accountAddress,
      invoiceID   = None,
      paths       = None,
      sendMax     = None,
      deliverMin  = None,
    )
    engine.sendTxn(txCommon, paymentRq, from).map((txnResult: XrplEngineTxnResult) => txnResult.submitted)
  }

  def transferXrp(
      from: Wallet,
      to: AccountAddress,
      drops: CurrencyAmount.Drops,
      txCommon: TxCommon = txCommomDefault,
  )(using rpc: XrplEngine): IO[Submit.Rs] = {
    val paymentRq = PaymentTx(
      account     = from.accountAddress,
      amount      = drops,
      destination = to,
      invoiceID   = None,
      paths       = None,
      sendMax     = None,
      deliverMin  = None,
    )

    val response: IO[XrplEngineTxnResult] = rpc.sendTxn(txCommon, paymentRq, from)
    val submitted: IO[Submit.Rs]          = response.map(_.submitted)
    submitted
  }

  def transferFiat(
      from: Wallet,
      to: AccountAddress,
      fiatAmount: CurrencyAmount.FiatAmount,
      txCommon: TxCommon = txCommomDefault,
      paths: Option[PaymentPath],
  )(using
      engine: XrplEngine
  ): IO[Submit.Rs] = {
    val paymentRq = PaymentTx(
      account     = from.accountAddress,
      amount      = fiatAmount,
      destination = to,
      invoiceID   = None,
      paths       = None,
      sendMax     = None,
      deliverMin  = None,
    )

    engine.sendTxn(txCommon, paymentRq, from).map(_.submitted)
  }

  /**
    * Gets the latest validated balances for account, Drops and all
    * FiatCurrencies. It drops extra data on the FiatBalances to make a
    * simple(r) data object.
    */
  def checkBalances(account: AccountAddress)(using engine: XrplEngine): IO[NonEmptyList[CurrencyAmount]] = {
    import com.odenzo.xrpl.models.support.utils.convertors.Conversions.given
    import io.scalaland.chimney.dsl.*
    for {
      xrp        <- checkXrpAccountBalance(account)
      tlines     <- checkFiatAccountBalances(account)
      fiatAmounts = tlines.map(_.transformInto[FiatAmount]) // account => issuer
    } yield NonEmptyList[CurrencyAmount](xrp, fiatAmounts)
  }

  /**
    * Uses account_info to just return the XRP balance and the LedgerHandle the
    * balance is from.
    */
  def checkXrpAccountBalance(account: AccountAddress, atLedger: LedgerHandle = validated)(using
      engine: XrplEngine
  ): IO[CurrencyAmount.Drops] = {
    log.debug("Checking XRP Balance")
    val rq = AccountInfo.Rq(account = account, queue = false, signerLists = false, ledgerIndex = atLedger)
    engine.send[AccountInfo.Rq, AccountInfo.Rs](rq).map { (result: XrplEngineCommandResult[AccountInfo.Rs]) =>

      log.debug(s"Got the Response: $result")
      val res         = result.rs
      val isValidated = res.validated
      log.info(s"Got atLedger ${atLedger} and the answer was Validated: $isValidated with Index ${res.ledgerIndex}")
      log.info(s"XRP Balance (in Drops): ${res.accountData.balance}")
      res.accountData.balance
    }
  }

  /** This is a scrolling API but we do not do scrolling. */
  def checkFiatAccountBalances(account: AccountAddress, atLedger: LedgerHandle = validated)(using
      engine: XrplEngine
  ): IO[List[TrustLine]] = {
    val rq = AccountLines.Rq(account = account, ledgerIndex = atLedger.some, ledgerHash = None)
    engine.send[AccountLines.Rq, AccountLines.Rs](rq).map { result =>
      val res: AccountLines.Rs = result.rs
      val isValidated          = res.validated
      // if res.marker.nonEmpty then log.warn("Marker Present -- Skipping Scrolling on it though.")
      res.lines

    }
  }

  /**
    * Probably want to make this calculator multipliers or something that can be
    * dropped into CommonTx
    */
  type FeeEstimator = (ledgerFees: Fee.Rs) => Drops

  /** This also gets the ledger_current_index */
  def feeEstimates(using engine: XrplEngine): IO[Fee.Rs] = {
    log.debug(s"FeeEstimates with ${engine.engineName}")
    for {
      fees <- engine.send[Fee.Rq, Fee.Rs](Fee.Rq())
      rs    = fees.rs
      _     = log.debug(s"Ledger Fees: ${rs.asJson.noSpaces}")
    } yield rs
  }

  /**
    * Get the current LedgerIndex, useful for determning the maximum LedgerIndex
    * for a transaction. Could cache this with timestamp and project the
    * probably maxLedgerIndex at some forward time unless using a Standalone
    * server.
    */
  def currentLedgerIndex(using engine: XrplEngine): IO[LedgerCurrent.Rs] = {
    engine.send[LedgerCurrent.Rq, LedgerCurrent.Rs](LedgerCurrent.Rq()).map(_.rs)
  }

  def listAccountCurrencies(
      address: AccountAddress
  )(using engine: XrplEngine): IO[AccountCurrencies.Rs] = {
    val rq                                                        = AccountCurrencies.Rq(address, validated)
    val result: IO[XrplEngineCommandResult[AccountCurrencies.Rs]] =
      engine.send[AccountCurrencies.Rq, AccountCurrencies.Rs](rq)
    val warnings                                                  = result.map(_.warnings)
    val ledgerInfo: IO[Option[ResponseLedgerInfo]]                = result.map(_.ledgerInfo)
    val rs: IO[AccountCurrencies.Rs]                              = result.map(_.rs)
    rs
  }

  /**
    *   - We skip vetoed=obsolete or true.
    *     - ignore the enabled=true just for logging
    *     - ignore the supported=false - just for logging
    *
    * Note: The XrplLabs Docker image is version 1.0.0 as first/last/good bummer
    */
  def enableAllAmendments(using engine: XrplEngine): IO[Map[Hash256, Amendment]] = {
    for {

      features    <- engine.send[Feature.Rq, Feature.Rs](Feature.Rq(None, None)).map(_.rs.features.values)
      needsEnabled =
        features.filter(a => a.enabled || !a.supported || a.vetoed.contains("Obsolete") || a.vetoed.contains(false))

      _                = log.info(s"Found ${needsEnabled.size} amendments to enable")
      _                = log.info(s"Feautre Names to Set NotVetoed: ${needsEnabled.map(_.name)}")
      unvetoed        <- needsEnabled.toList.traverse { (featureId: Amendment) =>
                           for {
                             result <- engine.send[Feature.Rq, Feature.Rs](Feature.Rq(featureId.name.some, false.some))
                             _      <- engine.ledgerAccept
                             feature = result.rs.features.filter { case (id, amendment) => id == featureId }
                           } yield feature
                         }
      unvetoedInfo     = unvetoed.flatMap(_.values)
      _                = log.info(s"UnVetoed: ${unvetoedInfo.map((a: Amendment) => a.name).mkString("\n")}")
      _               <- engine.ledgerAccept
      updatedFeatures <- engine.send[Feature.Rq, Feature.Rs](Feature.Rq(None, None)).map(_.rs.features)

    } yield updatedFeatures

  }
}
