package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.Amendment
import com.odenzo.xrpl.models.data.models.atoms.Hash256
import io.circe.*
import io.circe.Decoder.Result
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.syntax.*
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.ByteVector

object Feature extends XrpCommand[Feature.Rq, Feature.Rs] {
  case class Rq() extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.FEATURE
  }

  // TODO: Enable better results with customer decoder.
  /**
    * "07D43DCE529B15A10827E5E04943B496762F9A88E3268269D69C44BE49E21104" : {
    * "enabled" : true, "name" : "Escrow", "supported" : true, "vetoed" : false
    * },
    *
    * @param features
    */
  case class Rs(features: Map[Hash256, Amendment]) extends XrpCommandRs derives ConfiguredCodec

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  object Rs:
   
    given Configuration       = Configuration.default.withSnakeCaseMemberNames

}
