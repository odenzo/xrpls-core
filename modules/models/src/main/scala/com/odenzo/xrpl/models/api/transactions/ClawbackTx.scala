package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.InvoiceHash
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Clawback an MPToken Part of MPToken Amendment
  * https://xrpl.org/docs/references/protocol/transactions/types/clawback
  * @param holder
  *   AccountID of the current owner of the MPToken
  * @param amount
  *   The amount to clawback, with the issuer field set to the account you want
  *   to clawback from.
  */
case class ClawbackTx(
    holder: Option[AccountAddress],
    amount: CurrencyAmount,
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.Clawback
}

object ClawbackTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
