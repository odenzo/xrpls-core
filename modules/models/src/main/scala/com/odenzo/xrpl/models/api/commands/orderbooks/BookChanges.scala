package com.odenzo.xrpl.models.api.commands.orderbooks

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.Command.BOOK_OFFERS
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.api.commands.serverinfo.Fee.{ Rq, Rs }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{ LedgerIndex, validated }
import com.odenzo.xrpl.models.data.models.monetary.{ BookCurrency, Script }
import com.odenzo.xrpl.models.data.ledgertree.nodes.OfferNode
import com.odenzo.xrpl.models.data.models.atoms.LedgerHash
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Gets a list of offers, aka order book, between two currencies. Note the
  * currency is either { "currency" : "XRP"} or { "currency" :"USD",
  * "issuer":"..." } form https://ripple.com/build/rippled-apis/#book-offers So,
  * thehack will be Option with Issuer, None = XRP. Not sure why I have this is
  * Transactions
  */
object BookChanges extends XrpCommand[BookChanges.Rq, BookChanges.Rs] {

  import BookCurrency.given
  case class Rq(
      ledgerHash: Option[LedgerHash]    = None,
      ledgerIndex: Option[LedgerHandle] = Some(validated),
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.BOOK_CHANGES
  }
  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(changes: List[JsonObject], ledgerIndex: Option[LedgerIndex], ledgerHash: Option[LedgerHash])
      extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
