package com.odenzo.xrpl.models.data.keys

import com.odenzo.xrpl.models.data.keys.XrpPublicKey.given
import io.circe.Codec

/**
  * A Wallet holds credentials and addresses for an account. It can be used to
  * sign txn etc. Normally a wallet is (initially) populated from a
  * WalletPropse.Rs but this is a simplified version.
  * @param accountAddress
  *   In XRPL Base-58 form
  * @param keyType
  *   secp256k1 or ed25519
  */
case class Wallet(
    accountAddress: String,
    keyType: KeyType,
    masterSeed: XrpSeed,
    publicKey: XrpPublicKey,
) derives Codec.AsObject

object Wallet {
  val GENESIS_MASTER_PASSPHRASE: String = "masterpassphrase"

}
