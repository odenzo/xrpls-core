package com.odenzo.xrpl.models.data.flags.flags

import cats.effect.syntax.all.*
import io.circe.{ Decoder, Encoder }

enum EnableAmendmentFlag(val value: Long):
  case tfGotMajority extends EnableAmendmentFlag(65536)
  case tfLostMajority extends EnableAmendmentFlag(131072)

object EnableAmendmentFlag:
  // I wonder if I can use Singleton types of the Int value with a trait, instead of some lookup
  given Encoder[AccountFlag] = Encoder.encodeLong.contramap(_.value)
  given Decoder[AccountFlag] = Decoder.decodeLong.emap { l =>
    AccountFlag.values.find(_.value == l).toRight(s"Invalid AccountFlag $l")
  }
