package com.odenzo.xrpl.models.data.models.keys

import cats.effect.*
import cats.effect.syntax.all.*

import cats.*
import cats.data.*
import cats.syntax.all.*

import cats.data.NonEmptyList
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, TxnSignature }
import io.circe.Decoder.Result
import io.circe.DecodingFailure.Reason
import io.circe.{ Decoder, DecodingFailure, Encoder, Json, JsonObject }
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.syntax.given

/** Double check codec, add some helpers to add a Signer, which appends to List */
case class Signers(signers: NonEmptyList[Signer]) {
  def addSigner(signer: Signer): Signers = Signers(signers :+ signer)
}

object Signers:
  given Configuration = CirceCodecUtils.capitalizeConfig

  def extractNestedSigner(jo: JsonObject): Either[String, Signer] =
    jo("Signer")
      .toRight("No Signer Field Found in Signers JSON")
      .flatMap(json => json.as[Signer].leftMap(err => err.getMessage))

  given Encoder[Signers] = Encoder.instance { signers =>
    signers
      .signers.map { (signer: Signer) =>
        JsonObject.singleton("Signer", signer.asJson)
      }.asJson
  }

  given Decoder[Signers] = Decoder[NonEmptyList[JsonObject]]
    .emap[NonEmptyList[Signer]](jol => jol.traverse(extractNestedSigner))
    .map((ls: NonEmptyList[Signer]) => Signers(ls))

  /* ```json "Signers": [ { "Signer": { "Account": "rsA2LpzuawewSBQXkiju3YQTMzW13pAAdW", "SigningPubKey":
   * "02B3EC4E5DD96029A647CFA20DA07FE1F85296505552CCAC114087E66B46BD77DF", "TxnSignature":
   * "30450221009C195DBBF7967E223D8626CA19CF02073667F2B22E206727BFE848FF42BEAC8A022048C323B0BED19A988BDBEFA974B6DE8AA9DCAE250AA82BBD1221787032A864E5"
   * } } , { "Signer": { "Account": "rUpy3eEg8rqjqfUoLeBnZkscbKbFsKXC3v", "SigningPubKey":
   * "028FFB276505F9AC3F57E8D5242B386A597EF6C40A7999F37F1948636FD484E25B", "TxnSignature":
   * "30440220680BBD745004E9CFB6B13A137F505FB92298AD309071D16C7B982825188FD1AE022004200B1F7E4A6A84BB0E4FC09E1E3BA2B66EBD32F0E6D121A34BA3B04AD99BC1"
   * } } ],``` */

//
// "Signers": [{
//            "Signer": {
//                "Account": "rsA2LpzuawewSBQXkiju3YQTMzW13pAAdW",
//                "SigningPubKey": "02B3EC4E5DD96029A647CFA20DA07FE1F85296505552CCAC114087E66B46BD77DF",
//                "TxnSignature": "30450221009C195DBBF7967E223D8626CA19CF02073667F2B22E206727BFE848FF42BEAC8A022048C323B0BED19A988BDBEFA974B6DE8AA9DCAE250AA82BBD1221787032A864E5"
//            }
//        }, {
//            "Signer": {
//                "Account": "rUpy3eEg8rqjqfUoLeBnZkscbKbFsKXC3v",
//                "SigningPubKey": "028FFB276505F9AC3F57E8D5242B386A597EF6C40A7999F37F1948636FD484E25B",
//                "TxnSignature": "30440220680BBD745004E9CFB6B13A137F505FB92298AD309071D16C7B982825188FD1AE022004200B1F7E4A6A84BB0E4FC09E1E3BA2B66EBD32F0E6D121A34BA3B04AD99BC1"
//            }
//        }],
