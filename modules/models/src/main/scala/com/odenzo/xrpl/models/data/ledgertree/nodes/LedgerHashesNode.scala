package com.odenzo.xrpl.models.data.ledgertree.nodes

import com.odenzo.xrpl.models.data.ledgertree.LedgerNodeIndex
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import io.circe.Decoder

/**
  * Documents where? Relatively short list of node pointers, guess they are all
  * to some other LedgerNodes. Maybe they are all directory nodes, check
  * sometime
  *
  * @param flags
  * @param hashes
  * @param lastLedgerSequence
  * @param index
  */
case class LedgerHashesNode(
    flags: Int,
    hashes: List[LedgerNodeIndex],
    lastLedgerSequence: LedgerIndex,
    index: LedgerNodeIndex,
) extends LedgerNode

object LedgerHashesNode {

  implicit val decode: Decoder[LedgerHashesNode] =
    Decoder.forProduct4(
      "Flags",
      "Hashes",
      "LastLedgerSequence",
      "index",
    )(LedgerHashesNode.apply)

}
