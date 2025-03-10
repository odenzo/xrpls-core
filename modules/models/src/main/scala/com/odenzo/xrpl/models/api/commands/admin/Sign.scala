package com.odenzo.xrpl.models.api.commands.admin

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.atoms.TxBlob
import com.odenzo.xrpl.models.data.models.keys.XrpSeed
import io.circe.JsonObject
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object Sign {
  val command: Command = Command.SIGN

  /** Rough Cut. Need to decide if its XRP Payment or not? */
  case class Rq(
      secret: XrpSeed,
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
