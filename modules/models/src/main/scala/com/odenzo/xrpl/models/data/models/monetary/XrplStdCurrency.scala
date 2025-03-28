package com.odenzo.xrpl.models.data.models.monetary

import cats.*
import cats.implicits.*
import io.circe.{ Decoder, Encoder }

private object convert {
  def toBinary(c: String): XrplCurrency = XrplCurrency.fromIsoLikeString(c)
}

/**
  * Predefined Scripts names, pseudo-ISO 4417 or whatever, must be three
  * characters in alphabet. These are enumerated, use XrpCurrency for additional
  * cases.
  */
enum XrplStdCurrency(val currency: XrplCurrency):
  case BTC extends XrplStdCurrency(convert.toBinary("BTC"))
  case USD extends XrplStdCurrency(convert.toBinary("USD"))
  case NZD extends XrplStdCurrency(convert.toBinary("NZD"))
  case AUD extends XrplStdCurrency(convert.toBinary("AUD"))
  case EUR extends XrplStdCurrency(convert.toBinary("EUR"))
  case THB extends XrplStdCurrency(convert.toBinary("THB"))
  case ETH extends XrplStdCurrency(convert.toBinary("ETH"))

  given show: Show[XrplStdCurrency] = Show.show[XrplStdCurrency] { c =>
    s"[${c.toString}]"
  }

object XrplStdCurrency:
  /** I am keeping just to bootstrap until I get to a point I need to use. */
  given Decoder[XrplStdCurrency] = Decoder.failedWithMessage[XrplStdCurrency]("XrplCurrency Decoder NIMP")

  given Encoder[XrplStdCurrency] = Encoder.encodeString.contramap(b => b.toString)

  def convertToBinary(c: String): XrplCurrency = XrplCurrency.fromIsoLikeString(c)
