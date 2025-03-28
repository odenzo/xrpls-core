package com.odenzo.xrpl.models.data.models.keys

import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }

enum KeyType derives ConfiguredEnumCodec:
  case secp256k1
  case ed25519

object KeyType:
  given Configuration = Configuration.default

  extension (kt: KeyType)
    def isEd25519: Boolean   = kt == ed25519
    def isSecp256k1: Boolean = kt == secp256k1
