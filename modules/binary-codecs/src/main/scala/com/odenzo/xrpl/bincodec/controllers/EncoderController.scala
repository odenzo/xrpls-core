package com.odenzo.xrpl.bincodec.controllers

import _root_.scodec.bits.{ BitVector, hex }
import com.odenzo.xrpl.common.utils.MyLogging
import io.circe.{ Json, JsonObject }
import io.circe.syntax.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.internal.definitions.{ FieldEntryRaw, FieldMetaData }
import com.odenzo.xrpl.models.scodecs.MetaData
import com.tersesystems.blindsight.{ Condition, Logger, LoggerFactory }
import io.circe
import scodec.{ *, given }

/**
  * This takes a JsonObject and converts it to XRPL Binary Format. The encoding
  * is based on Field and the Field Type, and also uses two modes.
  *   1. SigningEncoder discards all fields that aren't meant to be included
  *      while signing a tx_json
  *   2. SerializingEncoder uses all fields.
  */
object EncoderController extends MyLogging {
  // I actually want to pass the condition or logger through to all scodecs
  private given log: Logger = LoggerFactory.getLogger.withCondition(Condition.never)

  val topLevelSigningEncoder: Encoder[Json] = Encoder[Json] { json =>
    json.asObject match
      case Some(jobj) => Attempt.successful(encode(jobj, true))
      case None       => Attempt.failure(Err(s"Not a JsonObject"))
  }

  val topLevelSerializingEncoder: Encoder[Json] = Encoder[Json] { json =>
    json.asObject match
      case Some(jobj) => Attempt.successful(encode(jobj, false))
      case None       => Attempt.failure(Err(s"Not a JsonObject"))

  }

  /**
    * Pain: We need to prepend the field id, so we need to know the json member
    * name that points to the object. Also need to append the standard 0xe1 for
    * when the object ends. Hmm, maybe we need to do this for fields in
    * top-level object too, think not. The top level encoder should be adding
    * field id's and sorting already
    */
  val stObjectSigningEncoder: Encoder[Json] = Encoder[circe.Json] { json =>
    json.asObject match
      case Some(jobj) => topLevelSigningEncoder.encode(json).map((bits: BitVector) => bits ++ hex"e1".bits)
      case None       => Attempt.failure(Err(s"Not a JsonObject"))
  }

  val stObjectSerializingEncoder: Encoder[Json] = Encoder[circe.Json] { json =>
    json.asObject match
      case Some(jobj) => topLevelSerializingEncoder.encode(json).map((bits: BitVector) => bits ++ hex"e1".bits)
      case None       => Attempt.failure(Err(s"Not a JsonObject"))
  }

  /**
    * Encodes a complete JSON Object, typically the tx_json of a transaction If
    * for signing then non-signing fields are dropped. The very top level has no
    * end of object marker. If this is a transaction then the transaction header
    * still needs to be added. Short enough I should just make a
    * encodeTxn(name:String, content:JsonObject)
    */
  def encode(json: JsonObject, forSigning: Boolean)(using log: Logger): BitVector = {

    // We encode the fields one-by-one since no STObject End Marker is used
    log.debug(s"Encoding For Signing: $forSigning JSON Object: ${json.asJson.spaces4}")
    import com.odenzo.xrpl.common.utils.CatsUtils.given
    val fields: List[(BitVector, String, FieldMetaData, Json)] = preprocessJson(forSigning, json)
    log.info("Encoding For Signing Fields $fields")
    val bits: BitVector                                        = fields.foldMap(_._1)
    bits
  }

  /**
    * Gets all the fields in the JsonObject, gets the FieldMetaData and filters
    * out those that are not signing. Then looks up the SCODEC encoder and does
    * the encoding.
    * @return
    *   The encoded field in a tuple with extra debugging information just for
    *   grins. Once optimized make a fold based method that just returns 1
    *   BitVector
    */
  def preprocessJson(forSigning: Boolean, jobj: JsonObject): List[(BitVector, String, FieldMetaData, Json)] = {
    def filterSigning(fm: FieldMetaData)     = fm.isSigningField
    def filterSerializing(fm: FieldMetaData) = fm.isSerialized
    val filterFn                             = if forSigning then filterSigning _ else filterSerializing _

    // Want to try named tuples here.
    val output: List[(BitVector, String, FieldMetaData, Json)] = jobj
      .toList
      .mapFilter {
        case (fieldName, content) =>
          val fieldMetaData: FieldMetaData = MetaData.getFieldMetaData(fieldName)
          log.debug(s"$fieldName / $content with entry $fieldMetaData")
          val fieldId                      = fieldMetaData.fieldId
          log.info(s"FieldId: ${fieldId.packedBinary} DataTypeCode: ${fieldId.dataTypeCodeAsInt} FieldCode: ${fieldId.fieldCodeAsInt}")
          if filterFn(fieldMetaData) && content != Json.Null then
            val encoder: Encoder[Json] = ScodecResolver.codecForDataType(fieldMetaData).asEncoder
            val header: BitVector      = fieldMetaData.fieldId.packedBinary
            log.info(s"About to Encode $content using ${fieldMetaData.fieldName} of type ${fieldMetaData.dataTypeName}")
            val encoded: BitVector     = encoder.encode(content).require // These encoders should be adding field id?
            (header ++ encoded, fieldName, fieldMetaData, content).some
          else None
      }
      .sortBy(_._3)

    val fieldDump: String = output
      .mapWithIndex {
        case ((bv: BitVector, s: String, fmd: FieldMetaData, json: Json), index: Int) =>
          s"""
             |Field:  # ${index}  ${s} ${json.noSpaces}
             |FieldId: ${fmd.fieldName} ${fmd.fieldId.packedBinary.toHex} (fieldCode= ${fmd
              .fieldId.fieldCodeAsInt},typeCode=${fmd.fieldId.dataTypeCodeAsInt})
             |Bits: ${bv.toHex} (len= ${bv.size} )
             |""".stripMargin
      }.mkString("\n")
    // log.info(s"Fields $fieldDump")
    output
  }

}
