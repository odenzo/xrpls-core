package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.binary.{ FixedSizeBinary, XrpBinaryOps }
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import io.circe.Codec
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import scodec.bits.{ BitVector, ByteVector }

import scala.util.Try

case class CredentialType(v: ByteVector)

object CredentialType:
  def fromByteVector(bv: ByteVector): Try[CredentialType] = Try(CredentialType(bv))
  given Codec[CredentialType]                             = CirceCodecUtils.hexCodec.iemapTry[CredentialType](fromByteVector)(c => c.v)

case class Credential(issuer: AccountAddress, credentialType: CredentialType) derives ConfiguredCodec

object Credential:
  given Configuration = CirceCodecUtils.capitalizeConfig
