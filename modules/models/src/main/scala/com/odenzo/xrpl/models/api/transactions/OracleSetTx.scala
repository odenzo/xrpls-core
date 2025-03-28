package com.odenzo.xrpl.models.api.transactions

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.transactions.support.{ XrpTxn, XrpTxnType }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.blob.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Creates (or modifies) existing book order offer.
  *   - https://xrpl.org/docs/references/protocol/transactions/types/oracleset
  *
  * @param expiration
  * @param offerSequence
  * @param takerGets
  * @param takerPays
  * @param base
  */
case class OracleSetTx(
                        account: AccountAddress,
                        oracleDocumentID: Long,
                        provider: Blob,
                        URI: Blob,
                        lastUpdateTime: XrplTime,
                        assetClass: Blob, // ASCII BLOB as Hex, max 16
                        priceDataSeries: List[PriceData],
) extends XrpTxn derives ConfiguredCodec {

  def txnType: XrpTxnType = XrpTxnType.OfferCreate
}

object OracleSetTx {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

/**
  * This says baseAsset is a currency, but has example of 9127blahblah which
  * doesn't confirm to ISO Currency format. So not sure how to put in
  * XrplCurrency. So aborting this one for now.
  */
case class PriceData(baseAsset: String, quoteAsset: String, assetPrice: BigInt) derives ConfiguredCodec

object PriceData:
  given Configuration = Configuration.default
