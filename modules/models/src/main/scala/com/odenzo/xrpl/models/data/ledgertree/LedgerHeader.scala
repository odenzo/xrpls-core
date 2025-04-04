package com.odenzo.xrpl.models.data.ledgertree

import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.{ AccountHash, TxnHash }
import com.odenzo.xrpl.models.data.models.atoms.{ LedgerHash, RippleTime }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.derivation.{ Configuration, ConfiguredCodec }

//
///**
//  * You can look up ledger headers user ledger inquiry. Testing for this does
//  * that. If current ledger then no ledger_hash is returned but ledger_index
//  * used not ledger_current_index Again, `current` ledger information are a
//  * bane, and for no actual use-case (for me). The close_ will not be there.
//  * This one might be worth make LedgerHeader and LedgerHeaderClosed But do it
//  * this way, and decide between a case class mapping. Either
//  * LedgerHeader.or[LedgerClosedHeader] appraoch or LedgerHeader (w/ options)
//  * then LedgerHeader => LedgerCloseInfo
//  *
//  * Trtying LedgerClose and LedgerCurrent appraoch, where Closed can be Accepted
//  * or Validated. Anything but current.
//  * [[https://ripple.com/build/ledger-format/#tree-format]]
//  * [[https://ripple.com/build/ledger-format/#header-format]]
//  */
case class LedgerHeader(
    ledgerIndex: Option[LedgerIndex], // Noted as String Number or Number! F
    ledgerHash: Option[LedgerHash],
    accountHash: Option[AccountHash],
    closeTime: Option[RippleTime],
    closed: Boolean,
    parentHash: Option[LedgerHash],
    totalCoins: Option[Drops],
    transactionHash: Option[TxnHash],
    closeTimeResolution: Option[Int],
    closeFlags: Option[Int], // Actually their docs outdated. TODO: Have a dedicated model
    accepted: Option[Boolean],
    parentCloseTime: Option[RippleTime], // Not sure why this is missing on closed if we have a hash.
    accountState: Option[List[LedgerNodeIndex]], // optional field and may be empty array
    transactions: Option[List[LedgerNodeIndex]], // optional field and may be empty array
) derives ConfiguredCodec

object LedgerHeader {
  given Configuration = Configuration.default.withSnakeCaseMemberNames
}
