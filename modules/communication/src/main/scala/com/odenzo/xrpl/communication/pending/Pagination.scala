package com.odenzo.xrpl.communication.pending

import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax.*
import io.circe.{ Decoder, Json, JsonObject }
//
/**
  * This is usually an embedded in a Result top level response for requests that
  * do scrolling and Pagination. TODO: In Progress
  * @param ledger_index_max
  * @param ledger_index_min
  * @param limit
  */
case class Pagination(
    limit: Option[Int],
    marker: Option[Json],
    ledger_index_max: LedgerIndex,
    ledger_index_min: LedgerIndex,
) derives ConfiguredCodec {
  def hasMore: Boolean = marker.isDefined
}
//
object Pagination {
  given Configuration = Configuration.default
  val defaultPaging   =
    new Pagination(limit = Some(50), marker = None, LedgerHandle.WILDCARD_LEDGER, LedgerHandle.WILDCARD_LEDGER)
}
