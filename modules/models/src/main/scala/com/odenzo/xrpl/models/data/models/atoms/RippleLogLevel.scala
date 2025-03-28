package com.odenzo.xrpl.models.data.models.atoms

import cats.implicits.*
import io.circe.Decoder.decodeString
import io.circe.Encoder.encodeString
import io.circe.{ Decoder, Encoder }

enum RippleLogLevel {
  case TRACE extends RippleLogLevel
  case DEBUG extends RippleLogLevel
  case INFO extends RippleLogLevel
  case WARNING extends RippleLogLevel
  case ERROR extends RippleLogLevel
  case FATAL extends RippleLogLevel
}

object RippleLogLevel {
  given Encoder[RippleLogLevel] = encodeString.contramap(v => v.toString.toLowerCase)

  given Decoder[RippleLogLevel] = decodeString.map(s => RippleLogLevel.valueOf(s))
}
