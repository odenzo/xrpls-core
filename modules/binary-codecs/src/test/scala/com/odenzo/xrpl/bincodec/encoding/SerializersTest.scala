package com.odenzo.xrpl.bincodec.encoding

import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.bincodec.controllers.EncoderController
import com.tersesystems.blindsight.LoggerFactory
import io.circe.{ Json, JsonObject }
import io.circe.literal.*
import scodec.{ Attempt, DecodeResult }
import scodec.bits.*

class SerializersTest extends munit.FunSuite with MyLogging {

  private val log = LoggerFactory.getLogger

  import scodec.bits.ByteVector

  val inorder =
    json"""
                  {
                   "TransactionType": "EscrowCancel"   ,
                  "OfferSequence": 25   ,
                   "Account": "r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2"    ,
                   "Owner": "r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2"
                  }
  """

  val sample =
    json"""
      {
       "Owner": "r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2",
       "Account": "r4jQDHCUvgcBAa5EzcB1D8BHGcjYP9eBC2"    ,
       "TransactionType": "EscrowCancel"   ,
       "OfferSequence": 25
      }
    """

  val bin =
    hex"""1200042019000000198114EE5F7CF61504C7CF7E0C22562EB19CC7ACB0FCBA8214EE5F7CF61504C7CF7E0C22562EB19CC7ACB0FCBA"""

//  test("Decoding") {
//    val res: DecodeResult[JsonObject] = DecoderController.decode(bin.bits)
//
//  }

  test("Encoding in Order") {
    val res: BitVector = EncoderController.topLevelSerializingEncoder.encode(inorder).require
    assertNoDiff(res.bytes.toHex, bin.toHex)
  }
  //
  //  test("Encoding out of Order") { // Note this isn't testing nested.
  //    val res: BitVector = EncoderController.encode(sample.asObject.get, forSigning = false)
  //    res.bytes shouldEqual bin
  //  }
  // }
}
