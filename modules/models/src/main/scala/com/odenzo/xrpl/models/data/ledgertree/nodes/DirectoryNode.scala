package com.odenzo.xrpl.models.data.ledgertree.nodes







//package com.odenzo.xrpl.models.ledgertree.nodes
//
//import cats.Show
//import io.circe.Decoder
//import com.odenzo.xrpl.common.UInt64
//import com.odenzo.xrpl.models.atoms.{AccountAddr, RippleHash}
//import com.odenzo.xrpl.models.ledgertree.LedgerNodeIndex
//
///**
//  * Directory Node, this doesn't show up in delta records
// *
// * @param rootIndex
//  * @param indexes
//  * @param indexNext
//  * @param indexPrev
//  * @param owner
//  * @param takerPaysCurrency
//  * @param takerPaysIssuer
//  * @param taketGetsCurrency
//  * @param takerGetsIssuer
//  */
//case class DirectoryNode(
//    rootIndex: RippleHash,
//    indexes: List[LedgerNodeIndex],
//    indexNext: Option[UInt64],
//    indexPrev: Option[UInt64],
//    owner: Option[AccountAddress],
//    takerPaysCurrency: Option[String],
//    takerPaysIssuer: Option[String],
//    taketGetsCurrency: Option[String],
//    takerGetsIssuer: Option[String],
//) extends LedgerNode
//
//object DirectoryNode {
//
//  implicit val decoder: Decoder[DirectoryNode] = Decoder.forProduct9("RootIndex",
//                                                                     "Indexes",
//                                                                     "IndexNext",
//                                                                     "IndexPrevious",
//                                                                     "Owner",
//                                                                     "TakerPaysCurrency",
//                                                                     "TakerPaysIssuer",
//                                                                     "TakerGetsCurrency",
//                                                                     "TakerGetsIssuer",
//                                                                    )(DirectoryNode.apply)
//
//  implicit val show: Show[DirectoryNode] = Show.fromToString[DirectoryNode]
//}
