package com.odenzo.xrpl.communication.models

import cats.data.NonEmptyList
import io.circe.derivation.Configuration
import io.circe.syntax.given
import io.circe.{ Decoder, Encoder, JsonObject }

/** This is returned on a succesful Command execution. */
case class XrplEngineCommandResult[T: Decoder: Encoder](
    rs: T,
    warnings: Option[NonEmptyList[XrplWarning]],
    ledgerInfo: Option[ResponseLedgerInfo],
)

object XrplEngineCommandResult {
  given Configuration = Configuration.default.withSnakeCaseMemberNames

   given [T](using enc: Encoder[T]): Encoder[XrplEngineCommandResult[T]] =
    Encoder.instance[XrplEngineCommandResult[T]] { v =>
      JsonObject(
        "rs" := v.rs,
        "warnings" := v.warnings,
        "ledgerInfo" := v.ledgerInfo,
      ).toJson
    }
}
