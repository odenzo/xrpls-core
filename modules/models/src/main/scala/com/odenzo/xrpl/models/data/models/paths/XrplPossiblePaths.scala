package com.odenzo.xrpl.models.data.models.paths

import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe
import io.circe.*
import io.circe.derivation.Configuration
import io.circe.generic.semiauto.*

case class XrplPossiblePaths(
    pathsCanonical: List[PaymentPath],
    pathsComputed: List[PaymentPath],
    sourceAmount: CurrencyAmount,
) derives Codec.AsObject

object XrplPossiblePaths:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
