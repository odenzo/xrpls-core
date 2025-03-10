package com.odenzo.xrpl.models.api.commands.ledgerinfo

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{XrpCommand, XrpCommandRq, XrpCommandRs}
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.ledgertree.LedgerHeader
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.{Codec, JsonObject}
import io.circe.derivation.{Configuration, ConfiguredCodec}
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumSigner

//package com.odenzo.xrpl.apis.commands.ledgerinfo
//
//import com.odenzo.ripple.models.support.{ RippleRq, RippleRs }
//import io.circe.*
//import io.circe.generic.semiauto.*
//import io.circe.syntax.*

/**
  * https://xrpl.org/docs/references/http-websocket-apis/public-api-methods/ledger-methods/ledger
  * Read the docs, this is just partially implemented. Accounts is deprecated
  * The result is a pain because various things can be returned and in binary or
  * json format. So, lots of the handling is just to Json or JsonObject until I
  * feel the need to use a partitcular thing,
  * @todo
  *   This is pretty basic TODO: Lots more options here. Will never enable full.
  *   Owner funds and expand we need though
  *
  * @param transactions
  * @param accounts
  * @param ledger
  */
object Ledger extends XrpCommand[Ledger.Rq, Ledger.Rs] {
  case class Rq(
      transactions: Boolean     = true,
      accounts: Boolean         = false, // Deprecated and Admin Only
      ledgerIndex: LedgerHandle = LedgerHandle.validated,
      expand: Boolean           = true,
      ownersFunds: Boolean      = true,
      binary: Boolean           = true,
      queue: Boolean            = false,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.LEDGER
  }
  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(
      ledger: LedgerHeader,
      resultLedger: Option[JsonObject],
      queueData: Option[List[JsonObject]],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
