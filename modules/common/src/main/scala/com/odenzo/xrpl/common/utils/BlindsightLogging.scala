package com.odenzo.xrpl.common.utils

/** Blindsight Logging Utilities */
trait BlindsightLogging extends BlindsightUtils { self =>

  import com.tersesystems.blindsight.{ Logger, LoggerFactory }
  inline def getBlindsightLogger: Logger                      = LoggerFactory.getLogger
  inline def getBlindsightLogger(userClass: Class[?]): Logger = LoggerFactory.getLogger(userClass.getCanonicalName)

  val wireLog: Logger = LoggerFactory.getLogger("WireLog")

}
