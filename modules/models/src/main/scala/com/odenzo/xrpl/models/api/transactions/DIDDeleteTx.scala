package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.blob.*
import com.odenzo.xrpl.models.data.models.keys.XrpPublicSigningKey
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * At least one of URI, data, DIDDocument must be specified.
  *   - https://xrpl.org/docs/references/protocol/transactions/types/didset
  * @deprecated
  *   Not Implemented
  * @note
  * @todo
  *   This is not implemented and no immediate plans to do so.
  */
case class DIDDeleteTx(
    account: AccountAddress,
    signingPublicKey: XrpPublicSigningKey,
    // Not sure about TxnSignature, same as normal as automatically included via pipelines and just inconsistent docs?
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.Clawback
}

object DIDDeleteTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
