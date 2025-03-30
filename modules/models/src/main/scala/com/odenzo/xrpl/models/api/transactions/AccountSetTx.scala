package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import com.odenzo.xrpl.models.data.flags.flags.AccountFlag
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/** Set Options on a Ripple Account */
case class AccountSetTx(
    account: AccountAddress,
    setFlag: Option[AccountFlag],
    clearFlag: Option[AccountFlag] = None,
    transferRate: Option[Long]     = None,
) extends XrpTxn derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.AccountSet
}

object AccountSetTx {
  given Configuration = CirceCodecUtils.capitalizeConfig

}
