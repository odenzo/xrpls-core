package com.odenzo.xrpl.models.api.psuedotxn

import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, XrplTime }
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.Drops

/**
  * Current functionality, using FeeUnits instead of XRP.
  * https://xrpl.org/docs/references/protocol/transactions/pseudo-transaction-types/setfee
  */
case class SetFee(
                   baseFeeDrops: Drops,
                   reserveBaseFeeDrops: Drops,
                   reserveIncrementDrops: Drops,
                   date: XrplTime,
                   account: AccountAddress = AccountAddress.ACCOUNT_ZERO,
                   fee: Drops              = Drops(0),
)

/**
  * This is for when the XRPFees amendment has been enabled.
  * https://xrpl.org/docs/references/protocol/transactions/pseudo-transaction-types/setfee
  */
case class SetFeeAmendment(
                            baseFeeDrops: Drops,
                            reserveBaseDrops: Drops,
                            reserveIncrementDrops: Drops,
                            date: XrplTime,
)
