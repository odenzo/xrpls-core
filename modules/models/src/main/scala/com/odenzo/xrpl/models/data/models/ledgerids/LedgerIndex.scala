package com.odenzo.xrpl.models.data.models.ledgerids

import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex

/** Mucking around learning the gotchas of Scala 3 enums. See LegderHandle also. */
object LedgerIndex:
  val WILDCARD_LEDGER: LedgerIndex = LedgerHandle.LedgerIndex(-1L)
  val MAX: LedgerIndex             = LedgerHandle.LedgerIndex(4294967295L)
  val MIN: LedgerIndex             = LedgerHandle.LedgerIndex(-1L)

extension (li: LedgerHandle.LedgerIndex) def add(b: Long): LedgerHandle.LedgerIndex = LedgerHandle.LedgerIndex(li.i + b)
