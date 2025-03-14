package com.odenzo.xrpl.bincodec.scodecs

import com.odenzo.xrpl.models.scodecs.AdditionalScodecs
import com.tersesystems.blindsight.LoggerFactory
import scodec.DecodeResult
import scodec.bits.*
import spire.math.{ULong, UShort}

class TransactionTypeTest extends munit.FunSuite {

  private val log = LoggerFactory.getLogger

  // Not much use testing the basic ones but lets see.
  test("TranasctionType") {
    val name = "Payment"
    val bits = AdditionalScodecs.xrplTransactionType.encode(name).require
    assertEquals(bits.size, 16L)
    log.info(s"$name => $bits")
  }
//  test("UINT8") {
//    shouldSucceed(xrpuint8.encode(0)) shouldEqual hex"00".bits
//    shouldSucceed(xrpuint8.encode(255)) shouldEqual hex"FF".bits
//    shouldFail(xrpuint8.encode(-1))
//    shouldFail(xrpuint8.encode(256))
//  }
//
//  test("UINT16") {
//    shouldSucceed(xrpuint16.encode(0)) shouldEqual hex"0000".bits
//    shouldSucceed(xrpuint16.encode(UShort.MaxValue.toInt)) shouldEqual hex"FFFF".bits
//    shouldFail(xrpuint16.encode(-1))
//    shouldFail(xrpuint16.encode(UShort.MaxValue.toInt + 1))
//    shouldSucceed(xrpuint16.encode(20)) shouldEqual hex"0014".bits
//    shouldSucceed(xrpuint16.encode(20)) shouldEqual hex"0014".bits
//  }
//
//  test("UINT32") {
//    shouldSucceed(xrpuint32.encode(0)) shouldEqual hex"0000_0000".bits
//    shouldSucceed(xrpuint32.encode(4294967295L)) shouldEqual hex"FFFF_FFFF".bits
//    shouldFail(xrpuint32.encode(-1))
//    shouldFail(xrpuint32.encode(4294967295L + 1L))
//  }
//
//  test("UINT64 - Long") {
//
//    // shouldFail(xrpulong64.encode(ULong(-1)))   // Wrap-around no good
//    // shouldFail(xrpulong64.encode(ULong.MaxValue + ULong(1))) // Wrap-around no good
//
//    shouldSucceed(xrpulong64.encode(ULong(0L))) shouldEqual hex"0000_0000_0000_0000".bits
//    shouldSucceed(xrpulong64.encode(ULong(255L))) shouldEqual hex"0000_0000_0000_00FF".bits
//    shouldSucceed(xrpulong64.encode(ULong.MaxValue)) shouldEqual hex"FFFF_FFFF_FFFF_FFFF".bits
//
//    val decResult: DecodeResult[ULong] = shouldSucceed(xrpulong64.decode(hex"0000_0000_0000_00FF".bits))
//    decResult shouldEqual DecodeResult(ULong(255), BitVector.empty)
//
//  }
//
//  test("xrphex") {
//    xrphex(2).encode("ff").require shouldEqual hex"FF".bits
//    xrphex(4).encode("FAff").require shouldEqual hex"FAFF".bits
//  }
//
//  test("XRP Hash160") {
//
//    val hash160Str = "F".repeat(40)
//
//    val bits: BitVector = xrphash160.encode(hash160Str).require
//    bits.size shouldBe 160
//    bits.toIndexedSeq.forall(_ == true) shouldBe true
//  }

}
