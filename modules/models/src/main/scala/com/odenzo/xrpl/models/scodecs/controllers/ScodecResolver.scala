package com.odenzo.xrpl.models.scodecs.controllers

import _root_.scodec.*
import _root_.scodec.bits.{ BitVector, ByteVector, given, hex }
import _root_.scodec.codecs.*
import cats.*
import cats.data.*
import cats.implicits.*
import ch.qos.logback.classic.encoder
import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress.given
import com.odenzo.xrpl.models.data.models.fields.ids.FieldId
import com.odenzo.xrpl.models.internal.definitions.{ FieldEntryRaw, FieldMetaData }
import com.odenzo.xrpl.models.scodecs.AccountScodecs.accountIdCodec
import com.odenzo.xrpl.models.scodecs.AdditionalScodecs.xrplTransactionType
import com.odenzo.xrpl.models.scodecs.BasicScodecs.xrpuint16
import com.odenzo.xrpl.models.scodecs.{ AmountScodecs, MetaData, VL }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.{ Codec, Decoder, Encoder, * }
import spire.math.ULong

import scala.util.Try

/**
  * All of the known XRPL data types, bound to some scodec. Based on traversing
  * through JSON or Binary we pass in which data type to partse based on its
  * FieldId
  */
object ScodecResolver extends BlindsightLogging {

  private val log = LoggerFactory.getLogger

  import com.odenzo.xrpl.models.scodecs.AccountScodecs.*
  // import com.odenzo.xrp.bincodec.scodecs.AdditionalScodecs.*

  import com.odenzo.xrpl.models.scodecs.BasicScodecs.*
  // import com.odenzo.xrp.bincodec.scodecs.PathSetScodecs.*
//  import com.odenzo.xrp.bincodec.scodecs.STObjectScodec.*

  def transform2Json[T: JsonEncoder](rs: T): Attempt[Json] = {
    import io.circe.syntax.*
    Attempt.successful(rs.asJson)
  }

  private[controllers] def asScodecDecoder[T: JsonEncoder](codec: Codec[T]): Decoder[Json] =
    codec.asDecoder.emap { s => transform2Json(s) }

  /** This takes Json and turns it into Binary (BitVector). */
  private[controllers] def asScodecEncoder[T: JsonDecoder](codec: Codec[T]): Encoder[Json] = {
    codec.asEncoder.econtramap[Json] { (json: Json) =>
      json.as[T] match
        case Left(err)    => Attempt.failure(Err(s"Error decoding json ${err.getMessage}"))
        case Right(value) => Attempt.successful(value)
    }
  }

  private def asScodec[T: JsonEncoder: JsonDecoder](codec: Codec[T]): Codec[Json] = {
    Codec(asScodecEncoder(codec), asScodecDecoder(codec))
  }

  /** I need to specialize on AccountAddress I guess. */

  /**
    * Given the underlying XRP Datatype then get a Codec that starts after the
    * FieldId prefix. Combined datatypes for a field are handled in these
    * Codecs, so they work for Arrays of XRP Datatypes too TODO: Think about a
    * match with dependant types, where T is JSON before the T.as[Json] Or this
    * could be implemented as a casetype discriminator in scodec land once we
    * map a field to a type? Turn this into an actual Codec[String, Json], or
    * can we actually tag the atomic models (and Amount, AccountID, PathSet with
    * their underlying XRPL datatype
    */
  val codecsByType: Map[String, Codec[Json]] = Map(
    "AccountID" -> asScodec(variableSizeBytes(VL.xrpvl, accountAddressCodec)),
    "Amount" -> asScodec(AmountScodecs.amountCodec),
    "UInt16" -> asScodec(xrpuint16),
    "Transaction" -> asScodec(xrplTransactionType),
    // "PathSet" -> asScodec(xrplPathSet),
    "Vector256" -> asScodec(xrpvectorhash256),
    "UInt8" -> asScodec(xrpuint8),
    "UInt32" -> asScodec(xrpuint32),
    "UInt64" -> asScodec(xrpulong64),
    "Blob" -> asScodec(xrpblob), // Think this is variable sized bytes too
    "Hash256" -> asScodec(xrphash256),
    "Hash128" -> asScodec(xrphash128),
    "Hash160" -> asScodec(xrphash160),
    "Unknown" -> asScodec(xrpError[Int]("Unknown Data Type")),
    "Validation" -> asScodec(xrpError[Int]("Validation NIMP")),
    "LedgerEntry" -> asScodec(xrpError[Int]("LedgerEntry NIMP")),
    "None" -> asScodec(xrpError[Int]("DONE datatype not understood")),
    "NotPresent" -> asScodec(xrpError[Int]("NotPresent Data Type")),
    //    // "STArray" -> asScodec(xrpstarray),
    //    //
  )

  def codecForDataType(fieldId: FieldId): Codec[Json] =
    val metaData: FieldMetaData = MetaData.getFieldMetaData(fieldId.packedBinary) // This should match the decoding bits
    codecForDataType(metaData)

  def codecForDataType(fieldMetaData: FieldMetaData): Codec[Json] =
    val aliased: String = aliasDataTypeName(fieldMetaData)
    val aliasInfo       = if aliased != fieldMetaData.dataTypeName then s"::AliasedTo[$aliased]" else ""
    codecForDataType(aliased).withContext(
      s"FieldName[${fieldMetaData.fieldName}]::DataType[${fieldMetaData.dataTypeName}] $aliasInfo"
    )

  /**
    * Calling this must use the correct dataTypeName, which should go through
    * aliasDataTypeName typically.
    */
  def codecForDataType(dataTypeName: String): Codec[Json] =
    codecsByType
      .getOrElse(dataTypeName, throw IllegalArgumentException(s"No SCodec Codec Found for Data Type [$dataTypeName]"))
      .withContext(dataTypeName)

  /**
    * Not sure why I am aliasing TransactionType vs Transaction. For fields, the
    * AccountId is always JSON as an AccountAddress but the binary is just the
    * AccountId part. IF uses the munged AccountAddress scodec to deal with
    * this.
    */
  def aliasDataTypeName(fieldMetaData: FieldMetaData): String = {

    if fieldMetaData.fieldName == "TransactionType" then "Transaction"
    else fieldMetaData.dataTypeName
  }

}
