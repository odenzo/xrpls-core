package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data
import com.odenzo.xrpl.models.data.Amendment
import com.odenzo.xrpl.models.data.atoms.hash256.*
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

/**
  * https:
  * //xrpl.org/docs/references/http-websocket-apis/admin-api-methods/status-and-debugging-methods/feature
  */
object Feature extends XrpCommand[Feature.Rq, Feature.Rs] {

  /**
    * @param feature
    *   If present the Hash256 Amendment Id or the Amendement name. If None then
    *   vetoed is none and list them all
    * @param vetoed
    *   Set to false to not propogate a veto. Not sure if this will enable if
    *   its supported or not on stand-alone server.
    */
  case class Rq(feature: Option[String], vetoed: Option[Boolean]) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.FEATURE
  }
  object Rq                                                                                                    {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

  // TODO: Enable better results with customer decoder.

  /**
    * This response has a different structure depending on if listing all the
    * amendments or "voting" on just one. The canonical form is the case class,
    * but if voting then we munge the reponse into the Map with just one record.
    * @param features
    */
  case class Rs(features: Map[Hash256, Amendment]) extends XrpCommandRs {

    /**
      * If there is exactly one feature in the map, get it as (K,V) in Option,
      * else None
      */
    def getHeadIfOne: Option[(Hash256, Amendment)] =
      Option.when(features.sizeIs == 1)(features.head)
  }

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
            atKey.as[Amendment].map { a => Rs(Map(h256 -> a)) }
          }
        case None      => CirceCodecUtils.customFailure("No HashKey field found", c)
    }

    /** Encoder which uses the normalized form of this data structure */
    given oldEncoder: Encoder.AsObject[Rs]   = deriveEncoder[Rs]
    given fallbackCodec: Decoder[Feature.Rs] = deriveDecoder[Rs].or(root)
  }

}
