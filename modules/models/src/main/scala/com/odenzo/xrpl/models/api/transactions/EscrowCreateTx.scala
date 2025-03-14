package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

case class EscrowCreateTx(
    account: AccountAddress,
    amount: Drops, // This amount is encoded as String in docs!
    destination: AccountAddress,
    cancelAfter: Option[RippleTime] = None,
    finishAfter: Option[RippleTime] = None, // as above, need a new type for this.
    condition: Option[String]       = None, // preimage-sha-256 crypto-condition.
    destinationTag: Option[DestTag] = None,
    sourceTag: Option[SourceTag]    = None,
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.EscrowCreate
}

object EscrowCreateTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
