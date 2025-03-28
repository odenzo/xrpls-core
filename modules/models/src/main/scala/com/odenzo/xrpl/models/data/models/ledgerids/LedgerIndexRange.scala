package com.odenzo.xrpl.models.data.models.ledgerids

import cats.implicits.*
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import io.circe.*

import scala.util.Try

// FIXME: Abandonded LedgerIndexRange, might as well do though (with regex mapping to LedgerIndex for start,end
// Putting "start"-"end" in Encoders,Decoders
case class LedgerIndexRange(start: LedgerIndex, end: LedgerIndex) {}

object LedgerIndexRange:

  object Codecs:

    given Encoder[LedgerIndexRange] =
      Encoder.encodeString.contramap(lir => s"${lir.start.i}-${lir.end.i}")

    given Decoder[LedgerIndexRange] = Decoder.decodeString.emapTry { s =>
      Try {
        val regex = """^(\d*)-(\d*)$""".r
        regex
          .findFirstMatchIn(s).map { mat =>
            val start: Long = mat.group(1).toLong
            val end: Long   = mat.group(2).toLong
            LedgerIndexRange(LedgerIndex(start), LedgerIndex(end))
          }.getOrElse(throw IllegalArgumentException(s"LedgerIndexRange $s didn't match $regex"))
      }
    }
