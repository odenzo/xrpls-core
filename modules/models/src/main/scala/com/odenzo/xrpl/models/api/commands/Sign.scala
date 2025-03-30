package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.atoms.TxBlob
import com.odenzo.xrpl.models.data.keys.{ KeyType, XrpSeed }
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object Sign {
  val command: Command        = Command.SIGN
  private given Configuration = Configuration.default.withSnakeCaseMemberNames

  /** This doesn't support secret, specify keytype and (seedHex OR passphrase) */
  case class Rq(
      keyType: KeyType = KeyType.secp256k1,
      seed: Option[XrpSeed],
      passphrase: Option[String],
      txJson: JsonObject,
      feeMultMax: Int  = 10,
      feeDivMax: Int   = 1,
      offline: Boolean = false,
      apiVersion: Int  = 2,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.SIGN
  }

  case class Rs(txJson: JsonObject, txBlob: TxBlob, hash: TxnHash) extends XrpCommandRs derives ConfiguredCodec

}
