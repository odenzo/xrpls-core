package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import scodec.bits.ByteVector

// FIXME: Re-File under the Hash256 types
opaque type AccountTxnId <: ByteVector = ByteVector // Hash256

object AccountTxnId:
  given Codec[AccountTxnId] = CirceCodecUtils.hexCodec
