package com.odenzo.xrpl.communication.subscription

import com.odenzo.xrpl.models.data.models.atoms.{ LedgerHash, XrplTime }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerIndexRange
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.literal.json

/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/subscription-methods/subscribe#subscribe
  * Note: Only Websocket Version supported
  *
  * For the various Subscribe Requests we get a Stream of N "update" I put them
  * here for now, until I cross match if they (easily) are subsumes by existing
  * data classes. Would love to have a simple way to have a partial model class,
  * and all the other fields just leave in place as a JsonObject. Kiinda like
  * `strict` makes surs there are no extra fields no parsed, I want a
  * `remainer:JsonObject` jo.as[A].fproduct(a:A => jo - A.asJsonObject) = (A,
  * Remainder)
  */
trait SubcribeStreamRs

case class LedgerClosed(
                         feeBase: Drops,
                         feeRef: Drops,
                         ledgerHash: LedgerHash,
                         ledgerIndex: LedgerIndex,
                         ledgerTime: XrplTime,
                         reserveBase: Drops,
                         reserveInc: Drops,
                         txnCount: Long,
                         `type`: String, // ledgerClosed
                         validatedLedgers: LedgerIndexRange,
)

val ledgerMsg = json"""
{
"fee_base" : 10,
"fee_ref" : 10,
"ledger_hash" : "962DF9C25C71B618908C15454686188C6FC37D2A664ADDF0260D30A77C1C6736",
"ledger_index" : 11,
"ledger_time" : 792048423,
"reserve_base" : 10000000,
"reserve_inc" : 2000000,
"txn_count" : 0,
"type" : "ledgerClosed",
"validated_ledgers" : "2-11"
}
                      """
