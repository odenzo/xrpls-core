package com.odenzo.xrpl.models.ledgertree.nodes

import com.odenzo.xrpl.models.data.atoms.*
import com.odenzo.xrpl.models.data.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.atoms.UnsignedCodecs.given
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount
import io.circe.Decoder

/** See also docs for account root node. I am guessing this has delta too? */
case class PayChannelNode(
                           flags: Option[Long],
                           account: Option[AccountAddress],
                           sequence: Option[AccountTxnId], // Was TxnSequence
                           takerPays: Option[CurrencyAmount],
                           takerGets: Option[CurrencyAmount],
                           bookDirectory: Option[AccountAddress],
                           bookNode: Option[UInt64], // really an option
                           expiration: Option[XrplTime],
                           ownerNode: Option[UInt64], // LedgerNodeIndex type.
                           previousTxnId: Option[TxnHash],
                           previousTxnLgrSeq: Option[LedgerIndex],
                           index: Option[String], // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object PayChannelNode {

  implicit val decode: Decoder[PayChannelNode] =
    Decoder.forProduct12(
      "Flags",
      "Account",
      "Sequence",
      "TakerPays",
      "TakerGets",
      "BookDirectory",
      "BookNode",
      "Expiration",
      "OwnerNode",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "index",
    )(PayChannelNode.apply)

}
