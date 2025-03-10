package com.odenzo.xrpl.communication.websocket.subscription

import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }

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

object XrplStream {
  given Configuration = Configuration.default
}
