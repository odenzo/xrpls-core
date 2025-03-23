package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.common.utils.{ CirceCodecUtils, Foo }
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models
import com.odenzo.xrpl.models.data.models.Amendment
import com.odenzo.xrpl.models.data.models.atoms.Hash256
import io.circe.{ cursor, * }
import io.circe.Decoder.{ Result, failedWithMessage }
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.syntax.*
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.ByteVector

/**
  * https:
  * //xrpl.org/docs/references/http-websocket-apis/admin-api-methods/status-and-debugging-methods/feature
  */
object Feature extends XrpCommand[Feature.Rq, Feature.Rs] {

  /**
    * @param feature
    *   If present just list or apply vetoed to one Amendment
    * @param vetoed
    *   Set to false to not propogate a veto. Not sure if this will enable if
    *   its supported or not on stand-alone server.
    */
  case class Rq(feature: Option[Hash256], vetoed: Option[Boolean]) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.FEATURE
  }
  object Rq                                                                                                     {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  // TODO: Enable better results with customer decoder.
  /**
    * "07D43DCE529B15A10827E5E04943B496762F9A88E3268269D69C44BE49E21104" : {
    * "enabled" : true, "name" : "Escrow", "supported" : true, "vetoed" : false
    * }, FMe: resuot.features Maon on list all. On Make a change its result with
    * no features field, and exaclty one amendment I think. -- Seems like it
    * could just have the same list with one member to be consistent? Check the
    * RS Wire
    * @param features
    */
  case class Rs(features: Map[Hash256, Amendment]) extends XrpCommandRs

  object Rs {

    given Configuration = Configuration.default.withSnakeCaseMemberNames

    private[commands] def decodeHash256(key: String, c: ACursor): Either[DecodingFailure, Hash256] =
      Hash256.fromHex(key) match
        case Some(value) => Right(value)
        case None        => Left(DecodingFailure(s"Invalid Valid HashKey in Result: $key", c.history))

    // Quick Hack for alternative response where there is no features field, just a single result field with the
    // Hash256 as the field name.
    val root = Decoder.instance[Rs] { c =>
      /* Find the first key thats looks like valid 256 bit hex String, specific as only one and the only alterenative is
       * `status` */
      val hashKey: Option[String] = c.keys.flatMap { iter =>
        iter.collectFirst {
          case hash: String if hash.sizeIs == 64 => hash
        }
      }
      hashKey match
        case Some(key) =>
          decodeHash256(key, c).flatMap { h256 =>
            val atKey: ACursor = c.downField(key)
            atKey.as[Amendment].map(a => Rs(Map(h256 -> a)))
          }
        case None      => CirceCodecUtils.customFailure("No HashKey field found", c)
    }

    /** Encoder which uses the normalized form of this data structure */
    given oldEncoder: Encoder.AsObject[Rs] = deriveEncoder[Rs]
    given fallbackCodec: Decoder[Feature.Rs] = deriveDecoder[Rs].or(root)
  }

}
