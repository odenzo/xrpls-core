package com.odenzo.xrpl.models.data.flags.flags

/** Inidivual UINT32 flags */
enum OfferCreateFlags(val value: Int):

  case tfPassive extends OfferCreateFlags(65536)

  /**
    * Never becomes a ledger node, just tries to match current existing orders
    * or fails. (Partial fill I guess(
    */
  case tfImmediateOrCancel extends OfferCreateFlags(131072)

  /**
    * Same as immediate of cancel, tries to make existing offer but only if full
    * TakerPays amount can be matched. (I think this can be matched across
    * multiple other offers though, test)
    */
  case tfFillOrKill extends OfferCreateFlags(262144)

  /** Sell all offered even if get more than desired result. */
  case tfSell extends OfferCreateFlags(1048576)
