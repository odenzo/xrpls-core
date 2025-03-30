package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.AccountAddress
import io.circe.Decoder
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object SetRegularKeyTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

/**
  * @param account
  *   Master account
  * @param regularKey
  *   Null to remove, otherwise the regular key address to bind to master
  *   account/keys
  */
case class SetRegularKeyTx(account: AccountAddress, regularKey: Option[AccountAddress]) extends XrpTxn
    derives ConfiguredCodec {
  def txnType: XrpTxnType = XrpTxnType.SetRegularKey
}
