package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, AccountTxnNumber }
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Cancels an existing book order offer.
  * [[https://ripple.com/build/transactions/#offercancel]]
  */
case class OfferCancelTx(account: AccountAddress, offerSequence: AccountTxnNumber) extends XrpTxn
    derives ConfiguredCodec {

  def txnType: XrpTxnType = XrpTxnType.OfferCancel
}

object OfferCancelTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
