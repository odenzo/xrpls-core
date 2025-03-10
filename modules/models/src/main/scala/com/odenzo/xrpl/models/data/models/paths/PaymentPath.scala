package com.odenzo.xrpl.models.data.models.paths

import com.odenzo.xrpl.models.data.models.monetary.XrplStdCurrency
import io.circe
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import io.circe.*

import java.util.Currency

/** This may be a more general thing to move into models */
case class PaymentPath(hops: List[FiatPathStep]) derives Codec.AsObject
