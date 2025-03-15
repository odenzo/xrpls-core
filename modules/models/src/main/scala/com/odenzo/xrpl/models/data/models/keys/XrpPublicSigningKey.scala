package com.odenzo.xrpl.models.data.models.keys

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import io.circe.{ Codec, Encoder }
import scodec.bits.ByteVector

import java.security.KeyPair
import scala.util.Try

/**
  * Allows for precalculation of Signing Key in implementation dependant format
  * This should be treated as an Opaque Type For SECP keys this saves time
  * deriving signing key from master_seed each time. This will probably be
  * stashed away in memoized thing in signing as not used in Communication (?)
  *
  * This sucks and I want to remove it. Put the optimization and caching in the
  * signing lubrary as needed. This is really just the XrpPublicKey from the
  * outside.
  */

case class XrpPublicSigningKey(publicKey: XrpPublicKey) extends AnyVal

object XrpPublicSigningKey:

  def fromByteVector(bv: ByteVector): Try[XrpPublicSigningKey] =
    Try {
      val key = (XrpPublicKey.fromPublicKeyBytes(bv))
      XrpPublicSigningKey(key)
    }

  given Codec[XrpPublicSigningKey] = CirceCodecUtils.hexCodec.iemapTry(fromByteVector)(sk => sk.publicKey.bv)

//sealed trait SigningKey {
//  def signPubKey: String
//}
//
//case class SigningKeyEd25519(kp: AsymmetricCipherKeyPair, signPubKey: String) extends SigningKey
//case class SigningKeySecp256(kp: KeyPair, signPubKey: String) extends SigningKey
