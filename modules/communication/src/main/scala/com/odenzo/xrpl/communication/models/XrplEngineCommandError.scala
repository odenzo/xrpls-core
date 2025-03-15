package com.odenzo.xrpl.communication.models

import cats.data.NonEmptyList
import io.circe.derivation.Configuration
import io.circe.{ Decoder, Encoder, Json }

/**
  * The Json contains the initial request embedded, its actually the full
  * response if we got one. If any kind of exception was thrown its caught and
  * put in the exception and rethrown in an IO typically.
  */
case class XrplEngineCommandError(
    warnings: Option[NonEmptyList[XrplWarning]],
    errors: Option[NonEmptyList[XrplError]],
    exception: Option[Throwable],
    rs: Option[Json],
) extends Throwable("Exception Exucuting XrplCommand")

// TODO: Make some building so we can include a nested exception?

object XrplEngineCommandError:
  given Encoder[Throwable] = Encoder.encodeString.contramap(_.getLocalizedMessage)
  given Decoder[Throwable] = Decoder.decodeString.map(s => Throwable(s))
  given Configuration      = Configuration.default.withSnakeCaseMemberNames // Don't want to serialize throwable
