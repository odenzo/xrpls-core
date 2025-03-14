package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{XrpTxn, XrpTxnType}
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.flags.flags.AccountFlag
import io.circe.*
import io.circe.derivation.{Configuration, ConfiguredCodec}
import io.circe.syntax.*

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
