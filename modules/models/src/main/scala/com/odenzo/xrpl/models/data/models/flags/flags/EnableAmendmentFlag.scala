package com.odenzo.xrpl.models.data.models.flags.flags

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import io.circe.{ Decoder, Encoder }
import scodec.bits.{ BitVector, ByteVector, hex }

import scala.collection.immutable

enum EnableAmendmentFlag(val value: Long):
  case tfGotMajority extends EnableAmendmentFlag(65536)
  case tfLostMajority extends EnableAmendmentFlag(131072)

object EnableAmendmentFlag:
  // I wonder if I can use Singleton types of the Int value with a trait, instead of some lookup
  given Encoder[AccountFlag] = Encoder.encodeLong.contramap(_.value)
  given Decoder[AccountFlag] = Decoder.decodeLong.emap { l =>
    AccountFlag.values.find(_.value == l).toRight(s"Invalid AccountFlag $l")
  }
