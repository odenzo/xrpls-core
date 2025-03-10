package com.odenzo.xrpl.models.scodecs

//package com.odenzo.xrp.bincodec.scodecs
//
//import com.odenzo.xrp.bincodec.controllers.{ MetaData, GenericDataTypeCodecFn }
//import com.odenzo.xrpl.common.utils.ELogging
//import com.odenzo.xrpl.models.apis.models.fields.ids.FieldId
//import com.odenzo.xrpl.models.definitions.FieldEntry
//import com.tersesystems.blindsight.LoggerFactory
//import io.circe.Json
//import scodec.*
//import scodec.bits.*
//import scodec.codecs.*
//
///**
//  * The EncoderController deals with fields for Encoding. This currently is used
//  * for Decoding from Binary only.
//  */
//object FieldScodec extends ELogging {
//
//  private val log                     = LoggerFactory.getLogger
//  given xrpfield: Dec[(String, Json)] = Codec(xrpfieldEncoder, xrpfieldDecoder).withContext("Field Decoder")
//
//  val xrpfieldDecoder: Decoder[(String, Json)] = scodec.Decoder(decodeNextField _)
//  val xrpfieldEncoder: Encoder[(String, Json)] = scodec.Encoder[(String, Json)](encodeNextField _)
//  given xrpfield: Codec[(String, Json)]        = Codec(xrpfieldEncoder, xrpfieldDecoder).withContext("Field Decoder")
//
//  /** Data is the full remaining data stream. */
//  def decodeNextField(data: BitVector): Attempt[(String, Json)] = {
//    log.debug(s"Decoding  Field from ${data.size} bits \n " + s"Preview 100 Hex ${data.take(100 * 4).toHex}")
//    val fieldId: Codec[FieldId] = FieldIdScodec.xrpfieldid
//
//    val fieldEntry: FieldEntry = MetaData.getFieldEntry(fieldId)
//    log.debug(s"Found Field: ${pprint.apply(fieldEntry)} }")
//
//    /**
//      * This covers the problem that sometimes AccountId is VL Encoded and
//      * sometimes it is not. There may be some fields that have subfields (aside
//      * from STObject and Vector stuff). But that is handled in the `parent
//      * field`
//      */
//    val decoder: Decoder[Json] =
//      if fieldEntry.metadata.isVLEncoded && fieldEntry.metadata.xrpType == "AccountID"
//      then GenericDataTypeCodecFn.dynamicDecoder("AccountIdVL")
//      else GenericDataTypeCodecFn.dynamicDecoder(fieldEntry.metadata.xrpType)
//
//  }
//
////  // @todo Need to know if signing or serializing here for encoding, or do at STObject level?
////  // Or pre-processed all the JSON ahead of time (preferred)
////  protected def encodeNextField(tuple: (String, Json)): Attempt[BitVector] = {
////    val (fieldName, content)   = tuple
////    val fieldEntry: FieldEntry = MetaData.getFieldEntry(fieldName)
////
////    log.debug(s"Field ENtry: $fieldEntry")
////    FieldIdScodec.xrpfieldid.encode(fieldId).flatMap { id =>
////      // Quick Hack... I think all accounts will be VL Encoded if they are a field
////      // Nested will be encoded through the enclosing encoder directly call accountNoVL
////
////      log.debug(s"Encoding $typeName")
////      val body     = GenericDataTypeCodecFn.dynamicEncode(content, typeName)
////      body.map(b => id ++ b)
////    }
////
////  }
//}
