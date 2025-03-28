package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.InvoiceHash
import com.odenzo.xrpl.models.data.models.atoms.blob.*
import com.odenzo.xrpl.models.data.models.keys.XrpPublicSigningKey
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * At least one of URI, data, DIDDocument must be specified.
  *   - https://xrpl.org/docs/references/protocol/transactions/types/didset
  */
case class DIDSetTx(
    account: AccountAddress,
    signingPublicKey: XrpPublicSigningKey,
    URI: Option[Blob]         = None, // A Hex String
    data: Option[Blob]        = None,
    DIDDocument: Option[Blob] = None,
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.Clawback
}

object DIDSetTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
