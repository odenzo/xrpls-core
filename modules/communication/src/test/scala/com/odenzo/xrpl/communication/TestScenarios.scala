package com.odenzo.xrpl.communication

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.EngineFunctions.SendCmdFn
import com.odenzo.xrpl.communication.XrplEngine
import com.odenzo.xrpl.communication.rpc.RPCEngine
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.api.commands.NoRippleCheck.Role
import com.odenzo.xrpl.models.api.transactions.TrustSetTx
import com.odenzo.xrpl.models.api.transactions.support.TxCommon
import com.odenzo.xrpl.models.data.models.keys.KeyType
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.models.monetary.*
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.{ Drops, FiatAmount }
import com.odenzo.xrpl.models.internal.Wallet
import com.odenzo.xrpl.models.internal.Wallet.given
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.given
object TestScenarios {

  private val log = LoggerFactory.getLogger

  val mode = "rpc" // or "rpc"

  def createFundedAccount(amount: Drops, fundedBy: Wallet = Wallet.GENESIS, keyType: KeyType = KeyType.secp256k1)(using
      engine: XrplEngine
  ): IO[Wallet] = {
    println(s"Engine: $engine")
    for {
      wallet   <- TestHelpers.proposeWallet(keyType)
      _        <- TestHelpers.fundAccount(wallet, amount, fundedBy)
      _        <- engine.ledgerAccept
      xrpDrops <- TestHelpers.checkXrpAccountBalance(wallet.accountAddress, validated)
    } yield wallet
  }

  /**
    * Creates two accounts, funded by Genesis and create a trustline between
    * them (issuer=>reciever). LedgerAccept are issues, eventually should ensure
    * they are validated but not yet
    * @return
    *   Tuple(IssuerWallet,ReceiverWallet)
    */
  def createAccountsWithTrustLine(limit: FiatValue, currency: XrplCurrency)(using engine: XrplEngine) =
    log.info("Creating FundingAccounts w/TrustLine")
    for {
      issuerWallet   <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(5555), Wallet.GENESIS)
      _              <- IO(log.info(s"Issue Wallat ${issuerWallet}"))
      receiverWallet <- TestScenarios.createFundedAccount(CurrencyAmount.xrp(6666), Wallet.GENESIS)
      _              <- engine.ledgerAccept
      // Two Accounts - have the receiver open a trust line for 1000$QQQ from the issuer
      issuer          = issuerWallet.accountAddress
      issuerScript    = Script(currency, issuer)
      trustSet        = TrustSetTx(receiverWallet.accountAddress, issuerScript.amount("1000.00"))
      rs             <- engine.sendTxn(TxCommon.default, trustSet, receiverWallet)
      _              <- engine.ledgerAccept
      issuerOk       <-
        engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](NoRippleCheck.Rq(issuerWallet.accountAddress, Role.user))
      // TODO: Adjusts No Ripple Flags as Needed
      _               = log.info(s"IssuerOk: $issuerOk")
      receiverOk     <-
        engine.send[NoRippleCheck.Rq, NoRippleCheck.Rs](NoRippleCheck.Rq(receiverWallet.accountAddress, Role.user))
      _               = log.info(s"ReceiverOk: $receiverOk")

    } yield (issuerWallet, receiverWallet)
}
