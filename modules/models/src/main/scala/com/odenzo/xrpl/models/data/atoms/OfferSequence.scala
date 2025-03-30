package com.odenzo.xrpl.models.data.atoms

import io.circe.{ Decoder, Encoder }

case class OfferSequence(v: Long)

object OfferSequence {
  implicit val encoder: Encoder[OfferSequence] = Encoder.encodeLong.contramap[OfferSequence](_.v)
  implicit val decoder: Decoder[OfferSequence] = Decoder.decodeLong.map(OfferSequence(_))

}
