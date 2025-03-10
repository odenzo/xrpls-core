package com.odenzo.xrpl.models.data.models.constants

import com.odenzo.xrpl.common.binary.XrpBinaryOps

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import scodec.bits.ByteVector

/**
  * Bootstrapping a Ripple system always creates this Genesis Account sh-4.2#
  * ./rippled -q wallet_propose masterpassphrase { "id" : 1, "result" : {
  * "account_id" : "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh", "key_type" :
  * "secp256k1", "master_key" : "I IRE BOND BOW TRIO LAID SEAT GOAL HEN IBIS
  * IBIS DARE", "master_seed" : "snoPBrXtMeMyMHUVTgbuqAfg1SUTb",
  * "master_seed_hex" : "DEDCE9CE67B451D852FD4E846FCDE31C", "public_key" :
  * "aBQG8RQAzjs1eTKFEAQXr2gS4utcDiEC9wmi7pfUPTi27VCahwgw", "public_key_hex" :
  * "0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020",
  * "status" : "success", "warning" : "This wallet was generated using a
  * user-supplied passphrase that has low entropy and is vulnerable to
  * brute-force attacks." } }
  */
object GenesisAccount extends XrpBinaryOps {
  val keyType          = "secp256k1"
  val address: String  = "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh"
  val masterSeedBase58 = "snoPBrXtMeMyMHUVTgbuqAfg1SUTb"
  val masterSeedHex    = "DEDCE9CE67B451D852FD4E846FCDE31C"
  val RFC1751          = "I IRE BOND BOW TRIO LAID SEAT GOAL HEN IBIS IBIS DARE"
  val publicKeyBase58  = "aBQG8RQAzjs1eTKFEAQXr2gS4utcDiEC9wmi7pfUPTi27VCahwgw"
  val publicKeyHex     = "0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020"

}

/** The XRP Issuer Account */
object AccountZero {
  val addressBase58 = "rrrrrrrrrrrrrrrrrrrrrhoLvTp"
}

/**
  * Neutral Account used in Ledger Info. A go between. Will show up in
  * RippleState type of LedgerEntry
  */
object AccountOne {
  val addressBase58 = "rrrrrrrrrrrrrrrrrrrrBZbvji"
}
