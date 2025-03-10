package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.utils.{ CirceCodecUtils, CirceUtils }
import io.circe.{ Decoder, Encoder }
import scodec.bits.ByteVector

/**
  * Make this opaque type TxBlob = HexData
  * @param v
  *   Represents the transaction blob return from the sign command. This is Hex
  *   and no reason to have as Bits ( yet.)
  */
case class TxBlob(hex: String) extends AnyVal {
  def asByteVector: ByteVector = ByteVector.fromValidHex(hex) // Maybe need upcase alphabet
}

object TxBlob {
  given encoder: Encoder[TxBlob] = Encoder.encodeString.contramap[TxBlob](_.hex)
  given decoder: Decoder[TxBlob] = Decoder.decodeString.map(hex => TxBlob(hex))
}
