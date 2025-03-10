package com.odenzo.xrpl.models.data.models.keys

import io.circe.{ Decoder, Encoder }

opaque type XrpRFC1751Passphrase = String

object XrpRFC1751Passphrase:
  def fromStringUnsafe(s: String): XrpRFC1751Passphrase = s

  given Encoder[XrpRFC1751Passphrase] = Encoder.encodeString
  given Decoder[XrpRFC1751Passphrase] = Decoder.decodeString
