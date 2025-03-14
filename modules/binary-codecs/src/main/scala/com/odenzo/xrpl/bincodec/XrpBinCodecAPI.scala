package com.odenzo.xrpl.bincodec

import com.odenzo.xrpl.common.utils.MyLogging
import io.circe.Json
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.scodecs.BinCodecError
import com.odenzo.xrpl.bincodec.controllers.EncoderController
import com.tersesystems.blindsight.LoggerFactory
import scodec.Attempt
import scodec.bits.BitVector

import scala.util.{ Failure, Success, Try }
//import com.odenzo.xrp.bincodec.controllers.TypeSerializers
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.models.scodecs.AccountScodecs
import scodec.bits.ByteVector

/**
  * This has functionality to convert Tx JSON to binary format. When we go
  * exploring the ledger we also decode from binary format to Json Models (which
  * aren;t aligned anymore, as using CLIO negates the immediate need to trawl
  * the ledger.
  */
object XrpBinCodecAPI extends MyLogging {
  private val log = LoggerFactory.getLogger
  log.trace("RippleCodeAPI Setup")

  /**
    * This is for encoding a JSON Transaction to tx_json binary field including
    * only signing fields.
    */
  def encode(json: Json, forSign: Boolean): BitVector = {
    Try {
      if forSign
      then EncoderController.topLevelSigningEncoder.encode(json).require
      else EncoderController.topLevelSerializingEncoder.encode(json).require
    } match
      case Failure(exception) => throw BinCodecError.EncodingError(json, exception)
      case Success(value)     => value
  }

  //  /**
  //    * Binary Serialize the tx_json with all fields marked "isSerialized" per
  //    * Ripple Spec
  //    *
  //    * @param jsonObject
  //    *   The tx_json object, with auto-fillable fields filled
  //    * @return
  //    *   Binary format, equivalent to tx_blob when converted to Hex, no padding
  //    *   needed
  //    */
  //  def serializedTxBlob(jsonObject: Json): Either[BinCodecError, ByteVector] = {
  //    encode(jsonObject, forSign = false)
  //  }
  //
  //  /**
  //    * Binary Serialize the tx_json with all fields marked "isSigningField" per
  //    * Ripple Spec
  //    *
  //    * @param jsonObject
  //    *   The tx_json object, with auto-fillable fields filled
  //    *
  //    * @return
  //    *   Binary format, equivalent to tx_blob when converted to Hex, no padding
  //    *   needed
  //    */
  //  def signingTxBlob(jsonObject: Json): Either[BinCodecError, ByteVector] = {
  //    encode(jsonObject, forSign = true)
  //  }
  //
  //  /**
  //    * When doing multisigning, the signing address is appended (suffix) to the
  //    * serialized for signing bytes as part of the process to form the payload
  //    * for signing. This can be used for that. Rather simple now and just
  //    * converts Base58Check to bytes, but we go through the main path to do so.
  //    *
  //    * @param addressBase58Check
  //    *   An account address in Base58Check form, e.g.
  //    *   r9EP7xcWBAWEHhgtm4evqsHTJT4ZesJHXX
  //    * @return
  //    *   Array of bytes or an error. This currently is NOT VLEncoded, just raw
  //    *   bytes. The r and trailing checksum IS encoded.
  //    */
  //  def serializedAddress(addressBase58Check: String): Either[BinCodecError, ByteVector] = {
  //    XrpBinaryOps.fromXrpBase58(addressBase58Check).leftMap(s => BinCodecError.fromString(s))
  //  }
  //
  //  def parseJson(json: String): Either[BinCodecError, Json] =
  //    io.circe.parser.parse(json).leftMap(e => BinCodecError.MalformedJson(e))
  //
  // }
}
