package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.InvoiceHash
import com.odenzo.xrpl.models.data.models.flags.Flags
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import com.odenzo.xrpl.models.data.models.paths.PaymentPath
import io.circe.derivation.{ Configuration, ConfiguredCodec }

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

object PaymentTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
