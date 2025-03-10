package com.odenzo.xrpl.models.data.models

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import scodec.bits.ByteVector
import com.odenzo.xrpl.common.utils.CirceCodecUtils.hexCodec
import io.circe.Codec
case class Amendment(id: ByteVector, enabled: Boolean, name: String, supported: Boolean, vetoed: Boolean)
    derives ConfiguredCodec

object Amendment:
  given hexByteVectorCodec: Codec[ByteVector] = CirceCodecUtils.hexCodec
  given Configuration                         = Configuration.default.withSnakeCaseMemberNames
