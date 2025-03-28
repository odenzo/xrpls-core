package com.odenzo.xrpl.models.data.models.flags.flags

import scodec.bits.{ ByteVector, hex }

/**
  * These are concrete bitmasks flags and can have multiple bits set. MUltiple
  * bits are represented in generic BitMaskFlags
  */
enum LedgerFlags(val value: ByteVector):

  /** Indicates that the account has used its free SetRegularKey transaction. */
  case lsfPasswordSpent extends LedgerFlags(hex"00010000")

  /** Requires incoming payments to specify a Destination Tag. asfRequireDest */
  case lsfRequireDestTag extends LedgerFlags(hex"00020000")

  /**
    * This account must individually approve other users for those users to hold
    * this account's issuances asfRequireAuth
    */
  case lsfRequireAuth extends LedgerFlags(hex"00040000")

  /**
    * Client applications should not send XRP to this account. Not enforced by
    * rippled. asfDisallowXRP
    */
  case lsfDisallowXRP extends LedgerFlags(hex"00080000")

  /**
    * Disallows use of the master key to sign transactions for this account.
    * asfDisableMaster
    */
  case lsfDisableMaster extends LedgerFlags(hex"00100000")

  /**
    * This address cannot freeze trust lines connected to it. Once enabled,
    * cannot be disabled. asfNoFreeze
    */
  case lsfNoFreeze extends LedgerFlags(hex"00200000")

  /** All assets issued by this address are frozen.	asfGlobalFreeze */
  case lsfGlobalFreeze extends LedgerFlags(hex"00400000")

  /**
    * Enable rippling on this addresses's trust lines by default. Required for
    * issuing addresses; discouraged for others. asfDefaultRipple
    */
  case lsfDefaultRipple extends LedgerFlags(hex"00800000")
end LedgerFlags
