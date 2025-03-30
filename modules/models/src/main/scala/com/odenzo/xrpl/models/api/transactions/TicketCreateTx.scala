package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.*
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object TicketCreateTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

/**
  * Create Ticket to reserve account txn sequence numbers. Love it! But this
  * incurrs reserve costs. Not sure there is any way to delete and reclaim the
  * reserve once the transaction slots are filled. (Perhaps by deleting the
  * account?)
  */
case class TicketCreateTx(
    account: AccountAddress,
    ticketCount: Int,
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.EscrowCreate
}

