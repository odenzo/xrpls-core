package com.odenzo.xrpl.models.scodecs

import io.circe.JsonObject
import scodec.bits.*
import scodec.codecs.{ *, given }
import scodec.{ *, given }
import spire.syntax.all.given

object AdditionalScodecs {

  /* n addition to all of the above field types, the following types may appear in other contexts, such as ledger
   * objects and transaction metadata:
   *
   * Type Name Type Code Length-prefixed? Description Transaction 10001 No A "high-level" type containing an entire
   * transaction.
   * LedgerEntry 10002 No A "high-level" type containing an entire ledger object.
   * Validation 10003 No A "high-level" type used in peer-to-peer communications to represent a validation vote in the
   * consensus process.
   * Metadata 10004 No A "high-level" type containing metadata for one transaction.
   * UInt64 3 No A 64-bit unsigned integer. This type does not appear in transaction instructions, but several ledger
   * objects use fields of this type.
   * Vector256 19 Yes This type does not appear in transaction instructions, but the Amendments ledger object's
   * Amendments field uses this to represent which amendments are currently enabled. */

  /**
    * We should have an enum of all the transaction types and their binary
    * values. Add to TxnType enum in main code. TODO: Double check how many bits
    * this is. UInt16 in Long?
    */
  val xrplTransactionType: Codec[String] = uint16
    .xmap((num: Int) => MetaData.getTransactionName(num), (str: String) => MetaData.getTransactionTypeCode(str))
    .withContext("TransactionType")
    .withToString("TransactionType")

  val xrplLedgerEntryType: Codec[String] = uint32
    .xmap(
      (num: Long) => MetaData.getLedgerEntryName(num.toInt),
      (str: String) => MetaData.getLedgerEntryTypeCode(str),
    )
    .withContext("LedgerEntryType")
    .withToString("LedgerEntryType")

  val xrplValidation: Codec[JsonObject] = fail(Err(s"Validation Special Code Is Only for special cases :-)"))
}
