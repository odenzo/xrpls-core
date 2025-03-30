package com.odenzo.xrpl.models.data.ledgerids

import cats.*
import cats.effect.syntax.all.*
import cats.implicits.*
import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import com.tersesystems.blindsight.LoggerFactory
import io.circe.*
import io.circe.syntax.given

import scala.annotation.unused

/**
  * On request ledger_index field can be String or Int, we use LedgerHandle for
  * that. Typically for responses we will just use LedgerIndex, but sometimes
  * its in the field ledger_current_index and sometimes in ledger_index. We keep
  * those out of the responses, and do some magic in the WS and RPC
  * communication pipelines instead.
  *
  * . Turns out mixing case class and enum case (validated etc) in an enum is a
  * pain in the neck No Values since have parameters, also a pain with
  * LedgerConstant | LedgerIndex type or opaque type. Maybe revisit
  */

enum LedgerHandle:
  case validated // Goes to standard field name
  case closed
  case current
  case LedgerIndex(i: Long)

//  extension (li: LedgerHandle.LedgerIndex)
//    def add(b: Long): LedgerHandle.LedgerIndex = LedgerHandle.LedgerIndex(li.i + b)
//
//object LedgerIndex:
//  val WILDCARD_LEDGER: LedgerIndex = LedgerHandle.LedgerIndex(-1L)
//  val MAX: LedgerIndex             = LedgerHandle.LedgerIndex(4294967295L)
//  val MIN: LedgerIndex             = LedgerHandle.LedgerIndex(-1L)

object LedgerHandle extends BlindsightLogging {
  @unused
  private val log = LoggerFactory.getLogger

  val WILDCARD_LEDGER: LedgerIndex = LedgerHandle.LedgerIndex(-1L)
  val MAX: LedgerIndex             = LedgerHandle.LedgerIndex(4294967295L)
  val MIN: LedgerIndex             = LedgerHandle.LedgerIndex(-1L)

  given encLedgerIndex: Encoder[LedgerIndex]     = Encoder.encodeLong.contramap(_.i)
  given ledgerIndexDecoder: Decoder[LedgerIndex] = Decoder.decodeLong.map(i => LedgerHandle.LedgerIndex(i))

  /**
    * We Encode LedgerHandles for the Requests, the literals are never in
    * results but we handle for completeness
    */
  given Encoder[LedgerHandle] = Encoder.instance[LedgerHandle] {
    case v: LedgerHandle.LedgerIndex => v.asJson
    case LedgerHandle.current        => Json.fromString("current")
    case LedgerHandle.closed         => Json.fromString("closed")
    case LedgerHandle.validated      => Json.fromString("validated")
  }

  val decodeString: Decoder[LedgerHandle] = Decoder.decodeString.emap {
    case "current"   => LedgerHandle.current.asRight
    case "closed"    => LedgerHandle.closed.asRight
    case "validated" => LedgerHandle.validated.asRight
    case bad         => "Illegal Ledger Constant $bad".asLeft
  }

  given Decoder[LedgerHandle] = ledgerIndexDecoder.or(decodeString)

}
