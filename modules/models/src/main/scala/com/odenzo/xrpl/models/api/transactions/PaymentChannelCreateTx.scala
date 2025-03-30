package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.atoms.UnsignedCodecs.given
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.keys.XrpPublicKey.given
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object PaymentChannelCreateTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

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
                                   cancelAfter: Option[XrplTime],
                                   destinationTag: Option[DestTag],
                                   sourceTag: Option[SourceTag],
) extends XrpTxn derives ConfiguredCodec {

  val txnType: XrpTxnType = XrpTxnType.PaymentChannelCreate

}
