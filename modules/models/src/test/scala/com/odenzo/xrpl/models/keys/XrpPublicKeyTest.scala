package com.odenzo.xrpl.models.keys

import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.fromPublicKeyHex
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Json
import io.circe.syntax.EncoderOps
class XrpPublicKeyTest extends munit.FunSuite with BlindsightLogging {

  private val log = LoggerFactory.getLogger

  val publicKeyBase58           = "aBQgpSykrZC6BDo6c4uvmJk6Zg1uD7ZuubGYh8JeoBBBBqqy9BAe"
  val publicKeyHex              = "0365ADFD889C8406105B16B03890F47C8CBB4130B5B9E13C787E31841E0AEA2E29"
  val publicKeyJson: Json       = Json.fromString(publicKeyBase58)
  val public_key_hex_json: Json = Json.fromString(publicKeyHex)
  val keyA: XrpPublicKey        = XrpPublicKey.fromPublicKeyBase58(publicKeyBase58)
  val keyB: XrpPublicKey        = XrpPublicKey.fromPublicKeyHex(publicKeyHex)
  import XrpPublicKey.given
  import XrpPublicKey.Codecs.given
  test("Secp Basic") {

    println(s"KeyA: ${keyA.asHex}")
    println(s"KeyB: ${keyB.asHex}")
    assertEquals(keyA, keyB, "fromXX produced sakey keys")
    assertEquals(keyA.asHex, publicKeyHex)
  }

  test("JSON Encoding") {
    val encoded = keyA.asJson
    println(s"Encoded: ${encoded.spaces4}")
    assertEquals(encoded, publicKeyJson, "Encoded to Base58")
  }

  test("JSON Decoding") {
    val decodedResult = publicKeyJson.as[XrpPublicKey]
    decodedResult match
      case Left(err)  => fail(s"Failed to decode: ${err}")
      case Right(key) => assertEquals(key, keyA, "Decoded Result")
  }
}
