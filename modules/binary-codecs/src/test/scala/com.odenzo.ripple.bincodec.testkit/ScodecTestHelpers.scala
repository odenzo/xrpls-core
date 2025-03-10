package com.odenzo.ripple.bincodec.testkit

import com.tersesystems.blindsight.{ Logger, LoggerFactory }

trait ScodecTestHelpers {

  private val log = LoggerFactory.getLogger
  import scodec.*
  import scodec.codecs.*

  /** Usage: val lcodec = scodecLogger(myCodec) */
  def scodeclogger[A](using log: Logger): Codec[A] => Codec[A] = {
    logBuilder[A]((a, r) => log.debug(s"A: $a =>R: $r"), (b, r) => log.debug(s"B: $b => R: $r"))
  }
}

object ScodecTestHelpers extends ScodecTestHelpers
