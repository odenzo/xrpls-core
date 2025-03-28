package com.odenzo.xrpl.communication.subscription

import com.odenzo.xrpl.models.data.models.monetary.{ BookCurrency, Script, XrpCurrency, XrplStdCurrency }
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Codec, Decoder, Encoder }

case class OrderBook(takerPays: BookCurrency, takerGets: BookCurrency, snapshot: Boolean) derives ConfiguredCodec

object OrderBook {
  import com.odenzo.xrpl.models.data.models.monetary.BookCurrency.given
  given Configuration = Configuration.default.withSnakeCaseMemberNames

  def forXrp: XrpCurrency          = XrpCurrency()
  def forScript(s: Script): Script = s
}
