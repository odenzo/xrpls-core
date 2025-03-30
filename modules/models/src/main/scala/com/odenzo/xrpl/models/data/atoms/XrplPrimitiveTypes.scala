package com.odenzo.xrpl.models.data.atoms

/**
  * These are/will be the canonical XRPL core data types.
  * https://xrpl.org/docs/references/protocol/data-types/basic-data-types These
  * are also listed in the definitions file. This currently doesn't include:
  * LedgerEntry, Transaction, Validation, MetaData PathSet, XChainBridge which
  * are types but not model as data types yet.
  */
enum XrplPrimitiveTypes {
  case ACCOUNTID,
    AMOUNT,
    BLOB,
    HASH160,
    HASH256,
    STARRAY,
    STOBJECT,
    UINT8,
    UINT16,
    UINT32,
    UINT64,
    UNKNOWN,
    VALIDATION,
    VECTOR256,

}
