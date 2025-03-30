package com.odenzo.xrpl.models.data

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.atoms.hash256.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Codec, Decoder, Encoder }
import scodec.bits.ByteVector

object Amendment:
  given hexByteVectorCodec: Codec[ByteVector] = CirceCodecUtils.hexCodec
  given Configuration                         = Configuration.default.withSnakeCaseMemberNames

/**
  * @param id
  * @param enabled
  * @param name
  * @param supported
  * @param vetoed
  *   true or false or String (ie Obsolete)
  */
case class Amendment(
    enabled: Boolean,
    name: String,
    supported: Boolean,
    validations: Option[Int],
    vetoed: Option[Vetoed],
) derives ConfiguredCodec

object Vetoed:
  given Decoder[Vetoed] =
    Decoder[Boolean]
      .map(Vetoed.apply)
      .or(Decoder[String].map(Vetoed.apply))
      .or(Decoder.failedWithMessage("Could Not Decode Wonky vetoed Field"))

  given Encoder[Vetoed] = Encoder.instance {
    case Vetoed(v: Boolean) => Encoder.encodeBoolean(v)
    case Vetoed(v: String)  => Encoder.encodeString(v)

  }

case class Vetoed(vetoed: Boolean | String) extends AnyVal
