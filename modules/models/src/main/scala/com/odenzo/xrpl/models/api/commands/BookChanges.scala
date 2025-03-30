package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.atoms.LedgerHash
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.{ LedgerIndex, validated }
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
