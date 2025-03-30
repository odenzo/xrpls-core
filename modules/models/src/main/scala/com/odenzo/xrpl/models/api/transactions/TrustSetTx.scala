package com.odenzo.xrpl.models.api.transactions

/**
  * This opens up a line of "availability" or trust to accept a FiatCurrency
  * from a specific address. Its `trust line` because we will buy with something
  * and trust them to redeem in XRP or some other currency or asset either
  * online or offline really. e.g. tokenized gold, or Meme coin.
  */

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import com.odenzo.xrpl.models.data.flags.Flags
import com.odenzo.xrpl.models.data.monetary.{ CurrencyAmount, Quality }
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * @param account
  * @param limitAmount
  *   Limit amount
  * @param flags
  *
  * @param qualityIn
  * @param qualityOut
  */
case class TrustSetTx(
    account: AccountAddress,
    limitAmount: CurrencyAmount.FiatAmount,
    flags: Option[Flags]        = None,
    qualityIn: Option[Quality]  = None,
    qualityOut: Option[Quality] = None,
) extends XrpTxn derives ConfiguredCodec {
  val txnType: XrpTxnType = XrpTxnType.TrustSet
}

object TrustSetTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
