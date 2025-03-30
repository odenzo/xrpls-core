package com.odenzo.xrpl.communication.subscription

import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }

object XrplStream {
  given Configuration = Configuration.default
}

enum XrplStream derives ConfiguredEnumCodec {
  case book_changes
  case consenses
  case ledger
  case manifests
  case peer_status
  case transactions
  case transactions_proposed
  case server
  case validations
}
