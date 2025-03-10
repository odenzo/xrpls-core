package com.odenzo.xrpl.models.internal.definitions

import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * This can be loaded from JSON file or by calling the server_definitions
  * command API with uses ServerDefination.Rs instead of this.
  */
case class ServerDefinitionsData(
    types: Map[String, Int], // Not quite a UInt not sure about out of UINT domain, errors?
    fields: List[FieldEntryRaw], // FIELDS:List[List[<Name>,<FieldMetaData>], ..., ...]
    ledgerEntryTypes: Map[String, Int],
    transactionResults: Map[String, Int],
    transactionTypes: Map[String, Int],
) derives ConfiguredCodec

object ServerDefinitionsData {
  given Configuration = Configuration.default.withScreamingSnakeCaseMemberNames
}
