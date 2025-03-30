package com.odenzo.xrpl.models.ledgertree.nodes

import com.odenzo.xrpl.models.data.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.FiatAmount
import com.odenzo.xrpl.models.data.monetary.Quality
import com.odenzo.xrpl.models.ledgertree.LedgerNodeIndex
import io.circe.Decoder

// TODO: Need a RippleStateFlag
// Does this appear in delta or not?  I think so.
// In Progress
case class RippleStateNode(
    balance: Option[FiatAmount],
    lowLimit: Option[FiatAmount],
    highLimit: Option[FiatAmount],
    prevTxnId: Option[TxnHash],
    prevTxnLgrSeq: Option[LedgerIndex],
    lowNode: Option[LedgerNodeIndex],
    highNode: Option[LedgerNodeIndex],
    lowQualityIn: Option[Quality], // Should have a RippleQuality type
    lowQualityOut: Option[Quality],
    highQualityIn: Option[Quality],
    highQualityOut: Option[Quality],
) extends LedgerNode

object RippleStateNode {
  // Quick hack
  // Yet again there must be some way to do this!
  // In this particular case COnfigure a fieldMapper maybe
//  private val fields = "Balance" ::
//               "LowLimit" ::
//               "HighLimit" ::
//               "PreviousTxnID" ::
//               "PreviousTxnLgrSeq" ::
//               "LowNode" ::
//               "HighNode" ::
//               "LowQualityIn" ::
//               "LowQualityOut" ::
//               "HighQualityIn" ::
//               "HighQualityOut" ::
//               HNil
//  private val tupledFields: (String, String, String, String, String, String, String, String, String, String, String) =
//    fields.tupled

  // val listFields: immutable.Seq[String] = fields.toList

  implicit val decoder: Decoder[RippleStateNode] = Decoder.forProduct11(
    "Balance",
    "LowLimit",
    "HighLimit",
    "PreviousTxnID",
    "PreviousTxnLgrSeq",
    "LowNode",
    "HighNode",
    "LowQualityIn",
    "LowQualityOut",
    "HighQualityIn",
    "HighQualityOut",
  )(RippleStateNode.apply)

}
