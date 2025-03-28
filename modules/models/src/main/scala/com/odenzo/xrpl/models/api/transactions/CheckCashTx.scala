package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.hash256.*
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Send a Check. Receiver gets same Currency from Same issuer, or XRP. Supports
  * InvoiceId as additional field.
  */
case class CheckCashTx(
    account: AccountAddress,
    amount: Option[CurrencyAmount],
    deliverMin: Option[CurrencyAmount],
    checkID: Option[Hash256],
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.CheckCash
}

object CheckCashTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
