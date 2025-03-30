package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.*
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Don't understand the correct use case for this. I create an Escrow (account
  * to account) with cancel time, then try and cancel it before cancel time. Get
  * no persmission. Wait until after cancel time expires and....
  *
  * @param account
  * @param owner
  * @param offerSequence
  */
case class EscrowCancelTx(
    account: AccountAddress, // Not neded?
    owner: AccountAddress,
    offerSequence: AccountTxnNumber, // This is the sequence field in EscrowCreate
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.EscrowCancel
}

object EscrowCancelTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
