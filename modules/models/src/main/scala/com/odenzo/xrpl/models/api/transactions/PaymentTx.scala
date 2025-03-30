package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import com.odenzo.xrpl.models.data.atoms.RippleHashes.InvoiceHash
import com.odenzo.xrpl.models.data.flags.Flags
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount
import com.odenzo.xrpl.models.data.paths.PaymentPath
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object PaymentTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

/**
  * V1 vs V2 differences. sendMax must be None for XRP-to-XRP. deliverMin can
  * skip but may result in partial payment? DeliverMax is V2.0.0 API .
  */
case class PaymentTx(
    account: AccountAddress,
    amount: CurrencyAmount,
    destination: AccountAddress,
    invoiceID: Option[InvoiceHash]     = None,
    paths: Option[PaymentPath]         = None,
    flags: Option[Flags]               = None,
    sendMax: Option[CurrencyAmount]    = None,
    deliverMin: Option[CurrencyAmount] = None,
) extends XrpTxn derives ConfiguredCodec {

  def txnType: XrpTxnType = XrpTxnType.Payment

}

