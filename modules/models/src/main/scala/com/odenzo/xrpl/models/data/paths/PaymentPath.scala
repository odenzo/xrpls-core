package com.odenzo.xrpl.models.data.paths

import io.circe
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.*

/** This may be a more general thing to move into models */
case class PaymentPath(hops: List[FiatPathStep]) derives Codec.AsObject
