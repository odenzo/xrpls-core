package com.odenzo.xrpl.models.internal.definitions

import com.odenzo.xrpl.models.data.models.fields.ids.FieldId
import io.circe.Codec

/**
  * Public Field Information constructred by processing the Raw Data from the
  * Config File The Json Codecs for this are just for logging.
  */
case class FieldMetaData(
    fieldName: String,
    dataTypeName: String,
    dataTypeCode: Int,
    nth: Int,
    isVLEncoded: Boolean,
    isSerialized: Boolean,
    isSigningField: Boolean,
) derives Codec.AsObject {
  val fieldId: FieldId = FieldId.fromFieldCodeAndDataTypeCode(nth, dataTypeCode)
}

object FieldMetaData {

  given Ordering[FieldMetaData] = Ordering.by(_.fieldId)
}
