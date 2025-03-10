package com.odenzo.xrp.bincodec.scodecs

import cats.implicits.*
import com.odenzo.xrpl.common.binary.XrplBase58Alphabet
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress.given
import com.odenzo.xrpl.models.data.models.constants.XrpConstants
import com.odenzo.xrpl.models.scodecs.AccountScodecs
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Json
import scodec.*
import scodec.bits.*

class AccountScodecsTest extends munit.FunSuite {

  private val log = LoggerFactory.getLogger

  val vlAcct1 = hex"81_14_0864D99FE19C6B19B0B7BA865B9A4A552173A896"
  val vlAcct2 = hex"83_14_D91693CB3D87723F716A16C46A398B9659864B0F"
  val acct1   = "rmPD5tJXdk3h4guoCsNADeDXRzmjvG3Ez"
  val acct2   = "rL8igKxCefdw8Jnmp2W4wpmgKjTfu1seKA"

  val genesisAddressStr: String = XrpConstants.GENESIS_ADDRESS
  val genesisAccountId          = AccountAddress.GENESIS.asAccountId

  import com.odenzo.xrpl.models.data.models.atoms.{ *, given }

  test("GENESIS AccountAddress Manual") {
    val accountAddressBytes: ByteVector =
      ByteVector.fromBase58Descriptive(genesisAddressStr, XrplBase58Alphabet).toOption.get
    log.info(s"AcccountAddressBytes: ${accountAddressBytes.bits}") // MF. This doesn't have prefix or
    val jsonAddrrB58                    = Json.fromString(genesisAddressStr)
    val addressDecoded: AccountAddress  = AccountAddress.given_Codec_AccountAddress.decodeJson(jsonAddrrB58) match
      case Left(value)  => throw Throwable("Failed", value)
      case Right(value) => value
    log.info(s"Decoded Address: ${addressDecoded.asBits}")

  }

  test("Genesis Address") {
    val address            = AccountAddress.GENESIS
    log.info(s"Address: ${address}")
    val encoded: BitVector = AccountScodecs.accountAddressCodec.encode(address).require
    log.info(s"Binary Encoded Address (AccoundId style): $encoded")
    val decoded            = AccountScodecs.accountAddressCodec.decode(encoded).require
    log.info(s"Decoded: ${decoded}")
    // assertEquals(decoded.value.toHex, genesisAddressHex)
  }

//  test("Leading Zero Account") {
//    val decResult: DecodeResult[String] = roundTripScodec(vlAcct1.drop(2).bits, acct1, xrplAccount)
//    decResult.remainder shouldBe empty
//  }
//
//  test("Acct2") {
//    val decResult: DecodeResult[String] = roundTripScodec(vlAcct2.drop(2).bits, acct2, xrplAccount)
//    decResult.remainder shouldBe empty
//  }

}
