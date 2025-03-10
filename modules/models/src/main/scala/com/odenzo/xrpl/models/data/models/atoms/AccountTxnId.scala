package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.{ Codec, Decoder }
import io.circe.Encoder
import scodec.bits.ByteVector

opaque type AccountTxnId <: ByteVector = ByteVector // Hash256

object AccountTxnId:
  given Codec[AccountTxnId] = CirceCodecUtils.hexCodec
