package com.odenzo.xrpl.common.binary

import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.tersesystems.blindsight.LoggerFactory
import munit.*
import scodec.bits.BitVector

/**
  * Well, now things are broken and I had no decent tests. The inbuild
  * ByteVector functionality should work for cases that don't start with `r`s.
  * XrpSeed is broken, and AccountAddress isn't happy either. Lets work with
  * some known data (Genesis account) to verify whats happening.
  */
class XrplBase58FixTest extends munit.FunSuite with BlindsightLogging {
  private val log = LoggerFactory.getLogger

  def roundTrip(base58Str: String) = {
    val fixed = XrpBase58Fix.fromValidXrpBase58(base58Str)
    val back  = XrpBase58Fix.toXrpBase58(fixed)
    log.info(s"""NIn:\t $base58Str
                |NFixed\t$fixed
                |NBack:\t $back""".stripMargin)

    val fixedO = BitVector.fromValidBase58(base58Str, XrplBase58Alphabet)
    val backO  = fixedO.toBase58(XrplBase58Alphabet)
    log.info(s"""OIn:\t $base58Str
                |Fixed\t$fixed
                |OBack:\t $backO
                |Back:\t $back""".stripMargin)

  }

  test("Roundtrips") {
    roundTrip("r")
    roundTrip("rr")
    roundTrip("sno")
  }

  test("Seed") {
    roundTrip("snoPBrXtMeMyMHUVTgbuqAfg1SUTb")
  }

}
