package com.odenzo.xrpl.common.utils

import com.tersesystems.blindsight.{ Logger, LoggerFactory }
import scodec.bits.{ BitVector, ByteVector }

/** Blindsight Logging Utilities */
trait BlindsightLogging extends BlindsightUtils { self =>
  // Which how I new how to get whatever this trait is mixed into and an macro or something.
  // protected val log: Logger = LoggerFactory.getLogger
  // Would be enough to export the imports.

  import com.tersesystems.blindsight.{ Logger, LoggerFactory }
  import scodec.bits.{ BitVector, ByteVector }
  inline def getBlindsightLogger: Logger                      = LoggerFactory.getLogger
  inline def getBlindsightLogger(userClass: Class[?]): Logger = LoggerFactory.getLogger(userClass.getCanonicalName)

  val wireLog: Logger = LoggerFactory.getLogger("WireLog")
}

class Foo extends BlindsightLogging {
  val log: Logger = getBlindsightLogger(getClass)
}
