package com.odenzo.xrpl.models.data.models

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import scodec.bits.ByteVector
import com.odenzo.xrpl.common.utils.CirceCodecUtils.hexCodec
import com.odenzo.xrpl.models.data.models.atoms.Hash256
import io.circe.{ Codec, Decoder, Encoder }

/**
  * @param id
  * @param enabled
  * @param name
  * @param supported
  * @param vetoed
  *   true or false or String (ie Obsolete)
  */
case class Amendment(enabled: Boolean, name: String, supported: Boolean, vetoed: Option[Vetoed]) derives ConfiguredCodec

object Amendment:
  given hexByteVectorCodec: Codec[ByteVector] = CirceCodecUtils.hexCodec
  given Configuration                         = Configuration.default.withSnakeCaseMemberNames

case class Vetoed(vetoed: Boolean | String) extends AnyVal

object Vetoed:
  given Decoder[Vetoed] = Decoder[Boolean]
    .map(Vetoed.apply).or(Decoder[String].map(Vetoed.apply))
    .or(Decoder.failedWithMessage("Could Not Decode Wonky vetoed Field"))

  given Encoder[Vetoed] = Encoder.instance {
    case Vetoed(v: Boolean) => Encoder.encodeBoolean(v)
    case Vetoed(v: String)  => Encoder.encodeString(v)

  }
