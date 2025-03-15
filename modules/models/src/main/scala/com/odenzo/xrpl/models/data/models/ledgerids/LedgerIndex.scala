package com.odenzo.xrpl.models.data.models.ledgerids

import _root_.scodec.bits.ByteVector
import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.implicits.*
import cats.syntax.all.{ *, given }
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.tersesystems.blindsight.LoggerFactory
import io.circe.*
import io.circe.CursorOp.MoveUp
import io.circe.Decoder.Result
import io.circe.DecodingFailure.Reason.CustomReason
import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }
import io.circe.syntax.given
import spire.algebra.Field

import scala.Right
import scala.util.Try

/** Mucking around learning the gotchas of Scala 3 enums. See LegderHandle also. */
object LedgerIndex:
  val WILDCARD_LEDGER: LedgerIndex = LedgerHandle.LedgerIndex(-1L)
  val MAX: LedgerIndex             = LedgerHandle.LedgerIndex(4294967295L)
  val MIN: LedgerIndex             = LedgerHandle.LedgerIndex(-1L)

extension (li: LedgerHandle.LedgerIndex) def add(b: Long): LedgerHandle.LedgerIndex = LedgerHandle.LedgerIndex(li.i + b)
