package com.odenzo.xrpl.models.data.ledgertree.nodes

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.atoms.UnsignedCodecs.given
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * See also docs for account root node. I am guessing this has delta too?
  * @param account
  * @param flags
  * @param sequence
  * @param balance
  * @param ownerCount
  * @param prevTxnId
  * @param prevTxnLgrSeq
  * @param accountTxnId
  * @param regularKeyAddr
  * @param emailHash
  * @param messagePubKey
  * @param tickSize
  * @param transferRate
  * @param domain
  * @param index
  */
case class EscrowNode(
    account: Option[AccountAddress], // Think not optional
    destination: Option[AccountAddress], // Think not optional
    amount: Option[Drops],
    condition: Option[String], // SHA256?
    cancelAfter: Option[RippleTime],
    finishAfter: Option[RippleTime],
    sourceTag: Option[SourceTag],
    destinationTag: Option[DestTag],
    flags: Option[Long],
    ownerNode: Option[UInt64],
    prevTxnId: Option[TxnHash], // The 64 character hex index (key)  , proper name? LedgerNodeIndex?  Cannot
    prevTxnLgrSeq: Option[LedgerIndex], // LedgerSequence type...yeah should be called prevTxnLgrIndex then?
    index: Option[String], // Guessing this is a LedgerNodeIndex of this node.
) extends LedgerNode derives ConfiguredCodec

object EscrowNode {
  given Configuration = CirceCodecUtils.capitalizeConfig(List("Index"))

}
