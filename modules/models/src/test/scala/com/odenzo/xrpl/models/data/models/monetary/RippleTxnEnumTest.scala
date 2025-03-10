package com.odenzo.xrpl.models.data.models.monetary

//package com.odenzo.xrpl.models.atoms
//
//import com.typesafe.scalalogging.StrictLogging
//import io.circe._
//import io.circe.generic.auto._
//import io.circe.syntax._
//import org.scalatest.FunSuite
//
//class RippleTxnEnumTest extends FunSuite with StrictLogging  {
//
//    test("Encoding") {
//
//      RippleTxnType.values.foreach(v=> logger.info(s"Value: $v => ${v.asJson}"))
//
//      val decoded: Either[Error, RippleTxnType] = io.circe.parser.parse(""" "AccountSetTxn"  """).flatMap(_.as[RippleTxnType])
//      logger.info(s"Decoded: ${decoded}")
//
//      val simple: Json = RippleTxnType.SetRegularKey.asJson
//      logger.info(s"Plain Enough JSON: ${simple.noSpaces}")
//    }
//}
