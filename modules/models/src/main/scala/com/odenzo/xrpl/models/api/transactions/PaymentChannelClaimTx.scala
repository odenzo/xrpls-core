package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.RippleHashes.{ PaymentChannelHash, TxnHash }
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.keys.XrpPublicKey.given
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.Drops
import io.circe.Decoder
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object PaymentChannelClaimTx {
  given Configuration = CirceCodecUtils.capitalizeConfig

}
/**
  * Claim from a Payment Channel
  * [[https://ripple.com/build/transactions/#paymentchannelclaim]]
  */
case class PaymentChannelClaimTx(
    channel: PaymentChannelHash,
    balance: Option[Drops],
    amount: Option[Drops],
    signature: Option[TxnHash], // FIXME: Incorrect? TxnSignature?
    publicKey: Option[XrpPublicKey],
    // FIXME: Two new flags to add.

) extends XrpTxn derives ConfiguredCodec {

  val txnType: XrpTxnType = XrpTxnType.PaymentChannelClaim

}


