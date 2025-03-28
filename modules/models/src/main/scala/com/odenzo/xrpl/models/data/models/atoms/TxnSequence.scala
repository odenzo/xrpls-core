package com.odenzo.xrpl.models.data.models.atoms

/**
  * Confusing names. Is/was this the transaction sequence number in the Account.
  * e.g. the 4th transaction in the account
  */
object TxnSequence {}
//
//opaque type AccountTxnId <: ByteVector = ByteVector // Hash256
//
//given Codec[AccountTxnId] = CirceCodecUtils.hexCodec
//
