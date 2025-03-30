package com.odenzo.xrpl.signing.core

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, AccountId }
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import com.odenzo.xrpl.models.xrpconstants.TypePrefix
import com.tersesystems.blindsight.LoggerFactory
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.ByteVector

object DeriveAccountAddress extends XrpBinaryOps {

  private val log = LoggerFactory.getLogger

  /**
    * Given as a Public Key adds prefix and XRP checksum.
    *
    * @param publicKey
    *   secp265k or ed25519 public keys, and operates on 33 bytes always, this
    *   includes the 0xED marker for Ed25519
    * @return
    *   Ripple AccountAddress
    */
  def xrpPublicKey2address(publicKey: XrpPublicKey): IO[AccountAddress] = {
    import XrpPublicKey.*
    val publicKeyBytes             = publicKey.asRawKey
    println(s"Raw Public Key: ${publicKeyBytes.toHex(HexUppercase)}")
    val accountIdBytes: ByteVector = XrpBinaryOps.ripemd160(XrpBinaryOps.sha256(publicKeyBytes))
    val accountId                  = AccountId.fromBytes(accountIdBytes)

    IO(AccountAddress.fromAccountId(accountId))

  }

}
