package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.validated
import com.odenzo.xrpl.models.data.monetary.{ BookCurrency, Script }
import com.odenzo.xrpl.models.ledgertree.nodes.OfferNode
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Gets a list of offers, aka order book, between two currencies. Note the
  * currency is either { "currency" : "XRP"} or { "currency" :"USD",
  * "issuer":"..." } form https://ripple.com/build/rippled-apis/#book-offers So,
  * thehack will be Option with Issuer, None = XRP. Not sure why I have this is
  * Transactions
  */
object BookOffers extends XrpCommand[BookOffers.Rq, BookOffers.Rs] {

  import BookCurrency.given
  case class Rq(
      taker: Option[AccountAddress]  = None,
      takerGets: BookCurrency, // Script or script with XRP currency and no issuer
      takerPays: BookCurrency,
      ledgerHash: Option[LedgerHash] = None,
      ledgerIndex: LedgerHandle      = validated,
      limit: Option[Long]            = None,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.BOOK_OFFERS
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(offers: List[OfferNode]) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
