package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.InvoiceHash
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Send a Check. Receiver gets same Currency from Same issuer, or XRP. Supports
  * InvoiceId as additional field.
  *
  * @param account
  * @param owner
  * @param offerSequence
  */
case class CheckCancelTx(
    account: AccountAddress,
    checkID: Hash256,
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.CheckCreate
}

object CheckCancelTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
