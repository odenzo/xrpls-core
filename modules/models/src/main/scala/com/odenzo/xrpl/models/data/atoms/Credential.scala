package com.odenzo.xrpl.models.data.atoms

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.Codec
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import scodec.bits.ByteVector

import scala.util.Try

object CredentialType:
  def fromByteVector(bv: ByteVector): Try[CredentialType] = Try(CredentialType(bv))

  given Codec[CredentialType] = CirceCodecUtils.hexCodec.iemapTry[CredentialType](fromByteVector)(c => c.v)

case class CredentialType(v: ByteVector)

object Credential:
  given Configuration = CirceCodecUtils.capitalizeConfig

case class Credential(issuer: AccountAddress, credentialType: CredentialType) derives ConfiguredCodec
