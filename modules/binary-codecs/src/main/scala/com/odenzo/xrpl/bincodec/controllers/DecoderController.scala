package com.odenzo.xrpl.bincodec.controllers

//package com.odenzo.xrp.bincodec.controllers
//
//import com.odenzo.xrpl.common.utils.ELogging
//import com.tersesystems.blindsight.LoggerFactory
////import com.odenzo.xrp.bincodec.scodecs.FieldScodec
//import io.circe.{ Json, JsonObject }
//import scodec.bits.BitVector
//import scodec.{ Attempt, DecodeResult }
////
///**
//  * Top Level Decoding of a full message (in Binary form), expecting to be an
//  * STObject with no marker
//  */
//object DecoderController extends ELogging {
//
//  private val log = LoggerFactory.getLogger
//
//  def decode(bv: BitVector): DecodeResult[JsonObject] = {
//    log.debug(s"Decoding Top Vector of Fields from ${bv.size}")
//    val result = scodec.codecs.vector(FieldScodec.xrpfield).decode(bv).require
//    result.map(JsonObject.fromIterable)
//  }
//}
