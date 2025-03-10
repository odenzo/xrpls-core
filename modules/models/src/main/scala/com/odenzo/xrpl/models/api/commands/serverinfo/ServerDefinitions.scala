package com.odenzo.xrpl.models.api.commands.serverinfo

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.api.commands.accountinfo.AccountCurrencies.{ Rq, Rs }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import com.odenzo.xrpl.models.internal.definitions.{FieldEntryRaw, ServerDefinitionsData}
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.deriveCodec
import io.circe.syntax.*

/**
  * Get the definitions.json file from the server. Unclear if this is just
  * WebSocket or RPC too.
  */
object ServerDefinitions extends XrpCommand[ServerDefinitions.Rq, ServerDefinitions.Rs] {
  

  /** TODO: See if a case object is usefu instead */
  case class Rq() extends XrpCommandRq derives Codec.AsObject {
    def command: Command = Command.SERVER_DEFINITIONS
  }

  case class Rs(
      types: Map[String, Int], // Not quite a UInt not sure about out of UINT domain, errors?
      fields: List[FieldEntryRaw], // FIELDS:List[List[<Name>,<FieldMetaData>], ..., ...]
      ledgerEntryTypes: Map[String, Int],
      transactionResults: Map[String, Int],
      transactionTypes: Map[String, Int],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs {
    given Configuration = Configuration.default.withScreamingSnakeCaseMemberNames
  }
}
