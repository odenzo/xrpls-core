package com.odenzo.xrpl.models.internal.definitions

import io.circe.Decoder
import cats.effect.*
import cats.effect.syntax.all.*

import cats.*
import cats.data.*
import cats.syntax.all.*

/**
  * These are in an object and name is the JsonObject field name and value is
  * code
  */
case class XrpLedgerEntryTypeMeta(name: String, code: Int)

object XrpLedgerEntryTypeMeta {

  // Could summon[Map[String,Int] and map that I guess. same. same in the end
  given Decoder[List[XrpLedgerEntryTypeMeta]] = Decoder.decodeJsonObject.emap { jobj =>
    jobj.toList.traverse {
      case (name, json) => json.as[Int].bimap(_.getMessage, i => XrpLedgerEntryTypeMeta(name, i))
    }
  }

}
