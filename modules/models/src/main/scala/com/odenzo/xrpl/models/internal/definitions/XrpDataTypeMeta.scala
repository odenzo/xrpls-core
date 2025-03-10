package com.odenzo.xrpl.models.internal.definitions

import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import io.circe.{ Decoder, DecodingFailure }
import scodec.bits.ByteVector

/**
  * These are in an object and name is the JsonObject field name and value is
  * code. This maps all the Xrp "Primitive" data types.
  * @code
  *   8 bit bytevector for now, represents unsigned int, should always be
  *   positive
  */
case class XrpDataTypeMeta(name: String, code: ByteVector)

object XrpDataTypeMeta {

  given Decoder[List[XrpDataTypeMeta]] = Decoder.decodeJsonObject.emap { jobj =>
    jobj.toList.traverse {
      case (name, json) => json.as[Int].bimap(_.getMessage, i => XrpDataTypeMeta(name, ByteVector.fromInt(i, 1)))
    }
  }

}
