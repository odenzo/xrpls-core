package com.odenzo.xrpl.models.ledgertree

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.ledgertree.nodes.*
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.ledgertree.nodes.{
  AccountRootNode,
  AmendmentsNode,
  DirectoryNode,
  EscrowNode,
  FeeSettingsNode,
  LedgerHashesNode,
  LedgerNode,
  OfferNode,
  RippleStateNode,
  SignerListNode,
}
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.syntax.*

import scala.collection.immutable.Nil

/**
  * There is really only one of these, modified, created -- no deleted?
  *
  * @param modifiedNode
  *   Node in ledger that has been changed
  * @param createdNode
  *   A new node in the ledger
  */
case class AffectedLedgerNode(modifiedNode: Option[LedgerNodeDelta], createdNode: Option[LedgerNodeDelta])
    derives ConfiguredCodec

object AffectedLedgerNode {
  given Configuration = CirceCodecUtils.capitalizeConfig
}

/**
  * Base for containers like CreatedNode and ModifiedNode that are found in
  * AccountTx NOTE THIS IS USING DIFFERENT DECODERS FROM CIRCE_DERIVATION
  */
// For Dev! This is any of Created, Modified type now. For any type of node record (e.g. AccountRoot, RippleState)
case class LedgerNodeDelta(
    ledgerEntryType: String, // LedgerEntryType, // String
    ledgerIndex: LedgerNodeIndex, // 64-char Hex
    previousTxnID: Option[LedgerNodeIndex],
    previousTxnLgrSeq: Option[LedgerIndex],
    finalFields: Option[JsonObject],
    previousFields: Option[JsonObject],
    newFields: Option[JsonObject],
) derives ConfiguredCodec

object LedgerNodeDelta {
  given Configuration                       = CirceCodecUtils.capitalizeConfig
  def debugDump(v: LedgerNodeDelta): String = {
    val r = s"LedgerNodeDelta Dump of type ${v.ledgerEntryType}" ::
      "Final Fields: " + v.finalFields.map(_.asJson.spaces2).getOrElse("No Final Fields") ::
      s"Ledger (Node) Index: ${v.ledgerIndex.v}" ::
      "FModified Fields: " + v.previousFields.map(_.asJson.spaces2).getOrElse("No PrevFields") ::
      "Created Fields: " + v.newFields.map(_.asJson.spaces2).getOrElse("No Created Fields") ::
      Nil

    r.mkString("\n")
  }
}

// FIXME: Forgot what I was doing here. Basically sniffing the node type to choose the decoder it seems.
private object Utils {

  def ledgerNodeTypeToDecoder(ltype: String): Decoder[_ <: LedgerNode] = {

    val decoder: Decoder[_ <: LedgerNode] = ltype match {
      case "AccountRoot"   => Decoder[AccountRootNode]
      case "RippleState"   => Decoder[RippleStateNode]
      case "DirectoryNode" => Decoder[DirectoryNode]
      case "Escrow"        => Decoder[EscrowNode]
      case "Offer"         => Decoder[OfferNode]
      case "SignerList"    => Decoder[SignerListNode]
      case "FeeSettings"   => Decoder[FeeSettingsNode]
      case "LedgerHashes"  => Decoder[LedgerHashesNode]
      case "Amendments"    => Decoder[AmendmentsNode]
      case failed          =>
        val failDecode: Decoder[LedgerNode] =
          Decoder.failedWithMessage[LedgerNode](s"Discriminator Not Handled $failed")
        failDecode
    }
    decoder
  }
}

/** Start out with Object blob, move to Shapeless later */
case class DeltaFields[A <: LedgerNode](fields: JsonObject)
