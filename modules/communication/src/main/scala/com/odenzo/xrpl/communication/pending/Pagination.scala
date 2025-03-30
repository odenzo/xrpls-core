package com.odenzo.xrpl.communication.pending

import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Decoder, Json }

object Pagination {
  given Configuration = Configuration.default

  val defaultPaging =
    new Pagination(limit = Some(50), marker = None, LedgerHandle.WILDCARD_LEDGER, LedgerHandle.WILDCARD_LEDGER)
}


/**
  * This is usually an embedded in a Result top level response for requests that
  * do scrolling and Pagination. TODO: Not Implemented Yet
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
