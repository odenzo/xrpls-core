package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, DestTag, RippleTime, SourceTag, UInt32 }
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import com.odenzo.xrpl.models.data.models.atoms.UnsignedCodecs.given
import XrpPublicKey.Codecs.given

/**
  * Funding an existing payment channel.
  * [[https://ripple.com/build/transactions/#paymentchannelcreate]]
  */
case class PaymentChannelCreateTx(
    account: AccountAddress,
    amount: CurrencyAmount.Drops,
    destination: AccountAddress,
    settleDelay: UInt32, //  Time to wait before can close channel, unit not shown. Seconds?
    publicKey: XrpPublicKey,
    cancelAfter: Option[RippleTime],
    destinationTag: Option[DestTag],
    sourceTag: Option[SourceTag],
) extends XrpTxn derives ConfiguredCodec {

  val txnType: XrpTxnType = XrpTxnType.PaymentChannelCreate

}

object PaymentChannelCreateTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
