package com.odenzo.xrpl.models.data.models

import com.odenzo.xrpl.models.data.models.atoms.hash256.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex

/**
  * This is a object with ServerInfo command response we extract. Basically its
  * to get the XRP Fee functionality.
  */
case class ValidatedLedger(
    age: Int,
    base_fee_xrp: Long,
    hash: Hash256, // As Hex
    reserve_base_xrp: Long,
    reserve_inc_xrp: Long,
    seq: LedgerIndex,
)
