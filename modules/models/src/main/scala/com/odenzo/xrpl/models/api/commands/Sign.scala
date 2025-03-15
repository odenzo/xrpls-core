package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.atoms.TxBlob
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpSeed }
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object Sign {
  val command: Command = Command.SIGN

  /** This doesn't support secret, specify keytype and (seedHex OR passphrase) */
  case class Rq(
      keyType: KeyType = KeyType.secp256k1,
      seed: Option[XrpSeed], // Damn, our examples are master_seed_hex not master_seed
      passphrase: Option[String],
      txJson: JsonObject,
      feeMultMax: Int  = 10,
      feeDivMax: Int   = 1,
      offline: Boolean = false,
      apiVersion: Int  = 2,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.SIGN
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(txJson: JsonObject, txBlob: TxBlob, hash: TxnHash) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
