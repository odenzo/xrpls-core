package com.odenzo.xrpl.models.data.ledgertree.nodes







//package com.odenzo.xrpl.models.ledgertree.nodes
//
//import com.odenzo.xrpl.models.ledgertree.LedgerNodeIndex
//import io.circe.Decoder
//
///**
//  * https://ripple.com/build/transactions/#enableamendment
//  * https://ripple.com/build/amendments/
//  * Amendments
//  */
//case class AmendmentsNode(flags: Long, amendments: List[LedgerNodeIndex], index: LedgerNodeIndex) extends LedgerNode
//
//object AmendmentsNode {
//
//  implicit val decode: Decoder[AmendmentsNode] =
//    Decoder.forProduct3(
//      "Flags",
//      "Amendments",
//      "index"
//    )(AmendmentsNode.apply)
//
//}
//
//
//// TODO: Encode the amendment flags. May as well bit flag it, even though only two values now.
