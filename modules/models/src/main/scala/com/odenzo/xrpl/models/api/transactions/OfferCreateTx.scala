package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, AccountTxnNumber, XrplTime }
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object OfferCreateTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

/**
  * Creates (or modifies) existing book order offer.
  * https://ripple.com/build/transactions/#offercreate
  *
  * @param expiration
  * @param offerSequence
  * @param takerGets
  * @param takerPays
  * @param base
  */
case class OfferCreateTx(
                          account: AccountAddress,
                          expiration: Option[XrplTime] = None,
                          offerSequence: Option[AccountTxnNumber],
                          takerGets: CurrencyAmount,
                          takerPays: CurrencyAmount,
) extends XrpTxn derives ConfiguredCodec {

  def txnType: XrpTxnType = XrpTxnType.OfferCreate
}

