//package com.odenzo.xrpl.models.primitives
//
//import com.tersesystems.blindsight.LoggerFactory
//import io.circe.JsonObject
//import io.circe.syntax.given
//import spire.math.{ ULong, UShort }
//class UnsignedTests extends munit.FunSuite {
//  private val log = LoggerFactory.getLogger
//
//  test("BigInt ULong") {
//    log.info(s"LongMax: ${Long.MaxValue}")
//    val x: UInt64  = UInt64.fromBigInt(BigInt("9223372036854775807"))
//    val y: UInt64  = UInt64.fromBigInt(BigInt("9223372036854775808"))
//    log.info(s"To Big: ${y.asJson.spaces4} \n ")
//    val serialized = x.asJson
//    val inObject   = JsonObject.singleton("UINT64", x.asJson)
//    log.info(s"${serialized.spaces4} \n ${inObject.toJson.spaces4}")
//  }
//
//  test("Spire Overflow") {
//    val x: UShort = UShort(Long.MaxValue.toShort)
//    log.info(s"Overflowed UShort $x using ${Long.MaxValue}, max UShort was ${UShort.MaxValue}")
//    assertNotEquals(x.signed.toShort, Short.MaxValue)
//  }
//
//  test("ULong Overflow") {
//    intercept[IllegalArgumentException](UInt64.fromBigInt(BigInt("18446744073709551616")))
//  }
//}
