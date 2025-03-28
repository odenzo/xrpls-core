package com.odenzo.xrpl.models.api.psuedotxn

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.atoms.hash256.*
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, RippleTime }
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * https://xrpl.org/docs/references/protocol/transactions/pseudo-transaction-types/enableamendment
  *
  * Once an amendment is enabled you cannot disable it I guess. See the
  * `Feature` command for checking what amendments there are and their status.
  */
case class EnableAmendment(
    amendment: Hash256,
    account: AccountAddress = AccountAddress.ACCOUNT_ZERO,
    fee: Drops              = Drops(0),
) derives ConfiguredCodec

object EnableAmendment {
  given Configuration = CirceCodecUtils.capitalizeConfig
}
