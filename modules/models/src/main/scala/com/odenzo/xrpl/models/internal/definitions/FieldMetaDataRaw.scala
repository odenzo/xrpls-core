package com.odenzo.xrpl.models.internal.definitions

import cats.implicits.*
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/** This is the raw data from the config file. */
case class FieldMetaDataRaw(
    nth: Int,
    isVLEncoded: Boolean,
    isSerialized: Boolean,
    isSigningField: Boolean,
    xrpType: String, // FIXME: This text based should be linke to the numeric (Int) typeCode
) derives ConfiguredCodec {
  def matches(fieldCode: Int, typeName: String): Boolean = xrpType == typeName && fieldCode == nth
}

object FieldMetaDataRaw {

  given Configuration =
    Configuration.default.withTransformMemberNames(CirceCodecUtils.typeName)

}
