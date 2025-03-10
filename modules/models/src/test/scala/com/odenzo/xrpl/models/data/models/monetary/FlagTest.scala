package com.odenzo.xrpl.models.data.models.monetary

//package com.odenzo.xrpl.models.atoms
//
//import com.odenzo.xrpl.models.wireprotocol.CodecTesting
//import org.scalatest.FunSuite
//
//class FlagTest extends FunSuite with CodecTesting {
//
//  import io.circe.syntax._
//
//  test("Encode/Decode TxnFlags") {
//    PaymentFlag.values.foreach { (f: PaymentFlag) =>
//      logger.info(s"$f => ${f.asJson.spaces2}")
//      val decoded = f.value.asJson.as[PaymentFlag]
//      logger.info(s"${f.value} => ${decoded}")
//    }
//
//    val aFlag: PaymentFlag = PaymentFlag.tfNoDirectRipple
//    val json               = aFlag.asJson
//    logger.debug("Flag: " + json.spaces2)
//
//  }
//}
