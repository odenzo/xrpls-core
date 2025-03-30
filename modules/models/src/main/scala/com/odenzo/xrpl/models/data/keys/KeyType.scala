package com.odenzo.xrpl.models.data.keys

import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }

object KeyType:
  given Configuration = Configuration.default
  
  /** Extensions automatically added in companian, up a level requires an import */
  extension (kt: KeyType)
    def isEd25519: Boolean   = kt == KeyType.ed25519
    def isSecp256k1: Boolean = kt == KeyType.secp256k1

enum KeyType derives ConfiguredEnumCodec:
  case secp256k1
  case ed25519
