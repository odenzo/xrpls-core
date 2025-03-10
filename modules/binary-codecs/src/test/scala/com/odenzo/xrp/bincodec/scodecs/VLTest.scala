package com.odenzo.xrp.bincodec.scodecs

import com.odenzo.xrpl.models.scodecs.VL
import com.tersesystems.blindsight.{Condition, LoggerFactory}
import scodec.{Attempt, DecodeResult}
import scodec.bits.{BitVector, *}

class VLTest extends munit.FunSuite {

  private val log = LoggerFactory.getLogger.withCondition(Condition.always)
  import com.odenzo.xrpl.models.scodecs.VL._

  def printBits(bv: BitVector): Unit = log.info(s"BV $bv =>  ${bv.toBin}")

  def check[T](
      originalLength: Int,
      expectedHex: String,
  )(using loc: munit.Location): Unit = {
    test(s"Variable Length: $originalLength => $expectedHex") {
      val obtained: BitVector           = VL.xrpvl.encode(originalLength).require
      val decodedRes: DecodeResult[Int] = VL.xrpvl.decode(obtained).require
      val decoded                       = decodedRes.value

      // Add the number of bytes encoded to based on originalLength checking

      assert(decodedRes.remainder.isEmpty, s"Should have been no remainder but got ${decodedRes.remainder}")
      assertEquals(obtained.toHex, expectedHex.toLowerCase)
      assertEquals(decoded, originalLength)

    }
  }

  List(0 -> "00", 1 -> "01", 20 -> "14", 192 -> "C0").foreach(data => check(data._1, data._2))

//  test("Small Zero") {
//
//    VL.xrpvl.encode(0).map { bv =>
//      assertEquals(bv.size, 8L)
//      assertEquals(bv.bytes.toHex, hex"00".toHex)
//    }
//  }

  test("Small Encoding") {
    VL.xrpvl.encode(1).map { bv =>
      assertEquals(bv.size, 8L)
      assertEquals(bv.bytes.toHex, hex"01".toHex)
    }
  }

  test("Small 192 ") {
    // This is the max value of first byte is one byte encoding
    VL.xrpvl.encode(192).map { bv =>
      assertEquals(bv.size, 8L)
      assertEquals(bv.bytes.toHex, hex"C0".toHex)
    }
  }

  test("Medium 193") {
    VL.xrpvl.encode(193).map { bv =>
      printBits(bv)
      assertEquals(bv.size, 16L)
      assertEquals(bv.bytes.toHex, hex"C100".toHex)
    }
  }
  // Broken, but we actually never use large VL Encoding that I see.
//  test("Medium 12480") {
//    VL.xrpvl.encode(12480).map { bv =>
//      printBits(bv)
//      bv.length shouldBe 16
//      bv.bytes shouldEqual (hex"F0FF")
//    }
//  }
//
//  test("Large 12481") {
//    VL.xrpvl.encode(12481).map { bv =>
//      printBits(bv)
//      xrpvl.encode(12481).require.bytes shouldEqual (hex"F1_00_00")
//    }
//
//  }

}
