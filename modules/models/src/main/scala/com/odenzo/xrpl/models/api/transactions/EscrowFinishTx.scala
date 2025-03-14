package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, AccountTxnNumber }
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.*

case class EscrowFinishTx(
    owner: AccountAddress,
    offerSequence: AccountTxnNumber,
    condition: Option[String],
    fulfillment: Option[String], // preimage-sha-256 crypto-condition.

) extends XrpTxn derives ConfiguredCodec {

  def txnType: XrpTxnType = XrpTxnType.EscrowFinish

}

object EscrowFinishTx {
  given Configuration = CirceCodecUtils.capitalizeConfig

}
