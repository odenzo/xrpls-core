package com.odenzo.xrpl.communication.subscription

import com.odenzo.xrpl.models.data.monetary.{ BookCurrency, Script, XrpCurrency }
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Decoder, Encoder }

case class OrderBook(takerPays: BookCurrency, takerGets: BookCurrency, snapshot: Boolean) derives ConfiguredCodec

object OrderBook {
  import com.odenzo.xrpl.models.data.monetary.BookCurrency.given
  given Configuration = Configuration.default.withSnakeCaseMemberNames

  def forXrp: XrpCurrency          = XrpCurrency()
  def forScript(s: Script): Script = s
}
