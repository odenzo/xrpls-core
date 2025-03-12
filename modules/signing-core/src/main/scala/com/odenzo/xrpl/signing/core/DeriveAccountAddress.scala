package com.odenzo.xrpl.signing.core

import com.odenzo.xrpl.signing.common.binary.XrpBinaryOps
import scodec.bits.ByteVector
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.constants.TypePrefix
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import com.odenzo.xrpl.signing.core.secp256k1.DER.accountPrefix
import com.tersesystems.blindsight.LoggerFactory

object DeriveAccountAddress extends XrpBinaryOps {

  private val log = LoggerFactory.getLogger

  /**
    * Given as a Public Key adds prefix and XRP checksum.
    *
    * @param publicKey
    *   secp265k or ed25519 public keys, and operates on 33 bytes always, this
    *   includes the 0xED marker for Ed25519
    * @return
    *   Ripple Account Address Base58 encoded with leading r and checksum.
    */
  def accountPublicKey2address(publicKey: XrpPublicKey): IO[AccountAddress] = {
    import XrpPublicKey.*
    val publicKeyBytes        = publicKey.asRawKey
    val accountId: ByteVector = XrpBinaryOps.ripemd160(XrpBinaryOps.sha256(publicKeyBytes))
    val body: ByteVector      = TypePrefix.accountAddress.bv ++ accountId //
    val bytes: ByteVector     = body ++ XrpBinaryOps.xrpChecksum(body)
    IO.fromEither(
      AccountAddress
        .fromRawBytes(bytes) // Need a fromBytesNoWrapper
        .leftMap((v: String) => IllegalArgumentException(s"Bad Bytes for AccountAddress"))
    )
  }

}
