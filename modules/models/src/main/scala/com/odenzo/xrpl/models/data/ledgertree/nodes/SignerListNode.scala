package com.odenzo.xrpl.models.data.ledgertree.nodes

import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.atoms.UnsignedCodecs.given
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.Decoder

/** See also docs for account root node. I am guessing this has delta too? */
case class SignerListNode(
    signerQuoruim: Option[AccountAddress],
    signerEntries: Option[AccountTxnId], // Sequence of txn in account or what?
    signerListID: Option[CurrencyAmount],
    flags: Option[Long],
    ownerNode: Option[UInt64], // LedgerNodeIndex type.
    previousTxnId: Option[TxnHash],
    previousTxnLgrSeq: Option[LedgerIndex],
    index: Option[String], // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode

object SignerListNode {

  implicit val decode: Decoder[SignerListNode] =
    Decoder.forProduct8(
      "SignerQuorum",
      "SignerEntries",
      "SignerListID",
      "Flags",
      "OwnerNode",
      "PreviousTxnID",
      "PreviousTxnLgrSeq",
      "index",
    )(SignerListNode.apply)

}
