package com.odenzo.xrpl.signing.bridge.attic

//package com.odenzo.xrpl.signing
//
//import cats.*
//import cats.data.*
//import cats.syntax.all.given
//import com.odenzo.xrpl.models.apis.commands.admin.Sign
//import com.odenzo.xrpl.signing.crypto.seeds.TxnSignature
//import io.circe.syntax.*
//import io.circe.{ Json, JsonObject }
//
//object MoveTOSigningServer {
//
//  /**
//   * @param rq
//   * Full SignRq (SingingPubKey doesn't need to be filled)
//   * @return
//   * Left is error response SignRs, Right is success response
//   */
//  def processSignRequest(rq: Sign.Rq): Either[Json, Json] = {
//
//    // validateRequest(rq)
//
//    val ok: Either[Any, Any] = for {
//      _ <- validateCommandCorrect("sign", rq)
//      key <- extractKey(rq)
//      tx_json <- JsonUtils.findField("tx_json", rq).leftMap(err => ResponseError.kNoTxJson)
//      _ <- validateAutofillFields(tx_json)
//      withPubKey = tx_json.mapObject(_.add("SigningPubKey", key.signPubKey.asJson))
//      sig <- Sign.signToTxnSignature(withPubKey, key).leftMap(err => ResponseError.kBadSecret)
//
//      txBlob <- Sign.createSignedTxBlob(withPubKey, sig).leftMap(err => ResponseError.kBadSecret)
//      blobhex = ByteUtils.bytes2hex(txBlob)
//      hash = Sign.createResponseHashHex(txBlob)
//      success = buildSuccessResponse(rq, tx_json, sig, blobhex, hash, key.signPubKey)
//    } yield success
//
//    ok.leftMap(re => buildFailureResponse(rq, re))
//
//  }
//
//  def buildSuccessResponse(
//                            rq: Json,
//                            rqTxJson: Json,
//                            sig: TxnSignature,
//                            signedBlob: String,
//                            hash: String,
//                            signPubKey: String,
//                          ): Json = {
//
//    val rsTxJson: Json = rqTxJson.mapObject { obj =>
//      obj
//        .remove("SigningPubKey")
//        .add("SigningPubKey", signPubKey.asJson)
//        .add("TxnSignature", Json.fromString(sig.hex))
//        .add("hash", Json.fromString(hash))
//    }
//
//    val sortedTxJson: Json = rsTxJson.mapObject(o => sortFieldsDroppingNulls(o))
//    val result = JsonObject(("tx_blob" := signedBlob), ("tx_json" := sortedTxJson))
//
//    JsonObject
//      .singleton("id", findField("id", rq).toOption.asJson)
//      .add("result", result.asJson)
//      .add("status", Json.fromString("success"))
//      .add("type", Json.fromString("response"))
//      .asJson
//  }
//
//}
