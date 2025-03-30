package com.odenzo.xrpl.models.data.flags.flags

import scodec.bits.{ ByteVector, hex }

/** UInt32 */
enum TrustFlags(val value: Int, val bits: ByteVector):

  /**
    * Authorize the other party to hold issuances from this account. (No effect
    * unless using the asfRequireAuth AccountSet flag.) Cannot be unset
    */
  case tfSetfAuth extends TrustFlags(65536, hex"00010000")

  /**
    * Blocks rippling between two trustlines of the same currency, if this flag
    * is set on both.(See No Ripple for details)
    */
  case tfSetNoRipple extends TrustFlags(131072, hex"00020000")

  /** Clears the No -Rippling flag.(See No Ripple for details. */
  case tfClearNoRipple extends TrustFlags(262144, hex"00040000")

  /** "Freeze the trustline." */
  case tfSetFreeze extends TrustFlags(1048576, hex"01000000")

  /** Unfreeze the trustline */
  case tfClearFreeze extends TrustFlags(2097152, hex"00200000")

  /**
    * Flags for offers. These are BitMask style flags (or'ed together)
    * https://ripple.com/build/transactions/#offercreate
    */
end TrustFlags

//
//enum OfferCreateFlag(val value: Int):
//
//  case tfPassive extends OfferCreateFlag(65536)
//
//  /**
//    * Never becomes a ledger node, just tries to match current existing orders
//    * or fails. (Partial fill I guess(
//    */
//  case tfImmediateOrCancel extends OfferCreateFlag(131072)
//
//  /**
//    * Same as immediate of cancel, tries to make existing offer but only if full
//    * TakerPays amount can be matched. (I think this can be matched across
//    * multiple other offers though, test)
//    */
//  case tfFillOrKill extends OfferCreateFlag(262144)
//
//  /** Sell all offered even if get more than desired result. */
//  case tfSell extends OfferCreateFlag(1048576)
