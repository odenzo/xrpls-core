package com.odenzo.xrpl.models.api.commands

import cats.effect.syntax.all.*
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.internal.definitions.FieldEntryRaw
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Get the definitions.json file from the server. Unclear if this is just
  * WebSocket or RPC too.
  */
object ServerDefinitions extends XrpCommand[ServerDefinitions.Rq, ServerDefinitions.Rs] {

  /** TODO: See if a case object is usefu instead */
  case class Rq() extends XrpCommandRq derives Codec.AsObject {
    def command: Command = Command.SERVER_DEFINITIONS
  }

  object Rs {

    /** This overrides the default Configuration specific in XrpCommand */
    given Configuration = Configuration.default.withScreamingSnakeCaseMemberNames
  }

  case class Rs(
      types: Map[String, Int], // Not quite a UInt not sure about out of UINT domain, errors?
      fields: List[FieldEntryRaw], // FIELDS:List[List[<Name>,<FieldMetaData>], ..., ...]
      ledgerEntryTypes: Map[String, Int],
      transactionResults: Map[String, Int],
      transactionTypes: Map[String, Int],
  ) extends XrpCommandRs derives ConfiguredCodec

}
