package com.odenzo.xrpl.models.data.models.monetary

import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Codec, Decoder, Encoder }

/**
  * FOr BookOrders and Offers were we need to specify XRP as a Currency but it
  * has no Issuer. (Make into case object?). Note it seems you have to manually
  * import the given Cokdecs at client site.
  */
case class XrpCurrency(currency: String = "XRP") derives Codec.AsObject

type BookCurrency = XrpCurrency | Script

object BookCurrency {

  private val xrpCodec                         = summon[Codec[XrpCurrency]]
  private val scriptCodec                      = summon[Codec[Script]]
  def fromScript(script: Script): BookCurrency = script: BookCurrency
  val XRP: XrpCurrency                         = XrpCurrency()

  given Decoder[BookCurrency]          = xrpCodec.map(v => v: BookCurrency).or(scriptCodec.map(v => v: BookCurrency))
  given Encoder.AsObject[BookCurrency] = Encoder.AsObject.instance[BookCurrency] {
    case x: XrpCurrency => xrpCodec.encodeObject(x)
    case s: Script      => scriptCodec.encodeObject(s)
  }

}
