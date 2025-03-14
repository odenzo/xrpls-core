package com.odenzo.xrpl.models.api.transactions.support



/**
  * For now just a marker on the core Tx (aka tx_json) types that need to be
  * signed and submitted in the proper envelope.
  */
trait XrpTxn:
  def txnType: XrpTxnType
