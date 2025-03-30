package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.atoms.RippleHashes.InvoiceHash
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount
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
case class CheckCreateTx(
                          destination: AccountAddress,
                          destinationTag: Option[DestTag],
                          sendMax: CurrencyAmount,
                          expiration: Option[XrplTime],
                          invoiceID: Option[InvoiceHash],
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.CheckCreate
}

object CheckCreateTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
