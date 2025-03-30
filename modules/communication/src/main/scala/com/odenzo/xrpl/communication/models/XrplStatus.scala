package com.odenzo.xrpl.communication.models

import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }

object XrplStatus:
  given Configuration = Configuration.default

/** RPC giving error on bad request. Maybe failure never occurs? */
enum XrplStatus derives ConfiguredEnumCodec:
  case success
  case failure
  case error

