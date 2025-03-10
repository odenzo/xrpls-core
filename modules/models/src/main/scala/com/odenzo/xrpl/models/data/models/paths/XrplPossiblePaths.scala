package com.odenzo.xrpl.models.data.models.paths

import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, XrplStdCurrency }
import io.circe
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import io.circe.*
import io.circe.derivation.Configuration

import java.util.Currency

case class XrplPossiblePaths(
    pathsCanonical: List[PaymentPath],
    pathsComputed: List[PaymentPath],
    sourceAmount: CurrencyAmount,
) derives Codec.AsObject

object XrplPossiblePaths:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
