package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.XrpTxnType.PaymentChannelFund
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.PaymentChannelHash
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, XrplTime }
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Funding an existing payment channel.
  * [[https://ripple.com/build/transactions/#paymentchannelfund]]
  */
case class PaymentChannelFundTx(
                                 account: AccountAddress,
                                 channel: PaymentChannelHash,
                                 amount: Drops = Drops(0),
                                 expiration: XrplTime,
) extends XrpTxn derives ConfiguredCodec {
  val txnType: XrpTxnType = PaymentChannelFund
}

object PaymentChannelFundTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
