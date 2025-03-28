package com.odenzo.xrpl.models.data.models

import com.odenzo.xrpl.models.data.models.atoms.{ AccountTxnNumber, XrplTime }
import com.odenzo.xrpl.models.data.models.flags.BitMaskFlags
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, Quality }
import io.circe.*
import io.circe.derivation.Configuration

/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/path-and-order-book-methods/book_offers
  * TODO: As used in account_offers inquiry and Book_offers. Needs testing with
  * both // I think this is implemented somewhere, just lost. OfferNode similar
  * but with comoon ledger node stuff. // @see TxOfferCreate and OfferNode
  *
  * This is an OfferNode from the Ledger objects with addition of
  */
case class OfferExtraFields(
                             expiration: Option[XrplTime],
                             flags: BitMaskFlags,
                             quality: Quality, // Redundant?
                             sequence: Option[AccountTxnNumber],
                             takerGets: CurrencyAmount,
                             takerPays: CurrencyAmount,
) derives Codec.AsObject

object OfferExtraFields:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
