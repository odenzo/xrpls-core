package com.odenzo.xrpl.models.data.ledgertree.nodes

import com.odenzo.xrpl.models.data.models.atoms.hash256.*
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, AccountTxnNumber, LedgerHash }
import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, Quality }
import io.circe.{ Decoder, Encoder }

/**
  * See also docs for account root node. I am guessing this has delta too?
  * [[https://ripple.com/build/rippled-apis/#book-offers]] is using this now
  * too. [[https://ripple.com/build/transactions/#offercreate]] FIXME: OfferNode
  * but see also TxNode TxOfferCreate. Referred to as OfferCreateNode in docs.
  * :-( Standard fields are capitalized, extra fields snakecase
  */
case class OfferNode(
    flags: Option[Long],
    account: Option[AccountAddress],
    sequence: Option[AccountTxnNumber],
    takerPays: Option[CurrencyAmount],
    takerGets: Option[CurrencyAmount],
    bookDirectory: Option[LedgerHash], // Hex, this a LeddgerHash
    bookNode: Option[String], // really an option
    //  expiration: Option[RippleTime],
    ownerNode: Option[String], // LedgerNodeIndex type? "0000000000000000" So, its a LedgerId?
    previousTxnId: Option[Hash256],
    previousTxnLgrSeq: Option[Long], // LedgerIndex as Long/Int, what is this, AccountLedgerSequence ?
    index: Option[String], // Transaction Index? Index wrt the consensus list of validated transactions?
    // Addition fields of book_offers API.
    // These are all snake case with no capitalization
    ownerFunds: Option[String], // XRP amount in Drops or a BigDecimal FiatValue
    quality: Option[Quality],
    takerGetsFunded: Option[CurrencyAmount],
    takerPaysFunded: Option[CurrencyAmount],
)

object OfferNode {
  // Name Transformers: Capitalize all excecpt the "index",
  //    "owner_funds",
  //    "quality",
  //    "taker_gets_funded",
  //    "taker_pays_funded",
  // and snake case those.
  // Probably less error prone.
  // A sane API V2 of this would be nice.
  given encode: Encoder[OfferNode] = Encoder.forProduct15(
    "Flags",
    "Account",
    "Sequence",
    "TakerPays",
    "TakerGets",
    "BookDirectory",
    "BookNode",
    //  "Expiration",
    "OwnerNode",
    "PreviousTxnID",
    "PreviousTxnLgrSeq",
    "index",
    "owner_funds",
    "quality",
    "taker_gets_funded",
    "taker_pays_funded",
  )(v =>
    (v.flags,
     v.account,
     v.sequence,
     v.takerPays,
     v.takerGets,
     v.bookDirectory,
     v.bookNode,
     v.ownerNode,
     v.previousTxnId,
     v.previousTxnLgrSeq,
     v.index,
     v.ownerFunds,
     v.quality,
     v.takerGetsFunded,
     v.takerPaysFunded,
    )
  )

  given decode: Decoder[OfferNode] =
    Decoder.forProduct15(
      "Flags",
      "Account",
      "Sequence",
      "TakerPays",
      "TakerGets",
      "BookDirectory",
      "BookNode",
      //  "Expiration",
      "OwnerNode",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "index",
      "owner_funds",
      "quality",
      "taker_gets_funded",
      "taker_pays_funded",
    )(OfferNode.apply)

}
