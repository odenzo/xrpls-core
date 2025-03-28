package com.odenzo.xrpl.models.internal.definitions

import cats.implicits.*
import io.circe.*

/**
  * This represents a field in the dump of XRPL configuration file
  * `definitions.json`. I wonder have Scala 3 tuples fit in here (T1,T2) <->
  * JsonList(T1, T2) with unname tuples or name's ignored
  */
case class FieldEntryRaw(name: String, metadata: FieldMetaDataRaw) // derives Codec.AsObject

object FieldEntryRaw {

  // Enter with c at the Array with two elements, 0=String, 1 = JsonObject
  implicit val decoder: Decoder[FieldEntryRaw] = new Decoder[FieldEntryRaw] {

    // No way to map an array to Hetorgenous list or tuples it seems
    final def apply(c: HCursor): Decoder.Result[FieldEntryRaw] =
      for {
        name     <- c.downArray.as[String]
        metadata <- c.downN(1).as[FieldMetaDataRaw]
      } yield {
        new FieldEntryRaw(name, metadata)
      }
  }

  given encoder: Encoder[FieldEntryRaw] = new Encoder[FieldEntryRaw] {
    import io.circe.syntax.*
    override def apply(a: FieldEntryRaw): Json = List(a.name.asJson, a.metadata.asJson).asJson
  }

}
