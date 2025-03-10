package com.odenzo.xrpl.models.data.models.keys

import cats.*
import cats.data.*
import cats.implicits.*
import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }
import io.circe.{ Decoder, Encoder }

enum KeyType derives ConfiguredEnumCodec:
  case secp256k1
  case ed25519

object KeyType:
  given Configuration = Configuration.default
