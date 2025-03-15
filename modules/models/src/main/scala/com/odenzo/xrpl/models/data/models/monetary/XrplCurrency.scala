package com.odenzo.xrpl.models.data.models.monetary

import _root_.scodec.bits.{ BitVector, ByteVector, hex }
import cats.*
import cats.data.{ Validated, ValidatedNec }
import cats.implicits.*
import com.odenzo.xrpl.common.binary.FixedSizeBinary
import com.tersesystems.blindsight.LoggerFactory
import io.circe.Decoder.{ Result, decodeValidated }
import io.circe.generic.semiauto.*
import io.circe.*

import java.nio.charset.{ CharacterCodingException, Charset, StandardCharsets }
import scala.collection.immutable
import scala.util.Try

/**
  * There are predefined currency, this is the underlying currency structure
  * which can actually be anything and can contain the XrplCurrency.
  *
  * This is just 160-bit binary data, with no constraints except it cannot start
  * with 0x00
  *
  * This is used with enum CurrencyAmount: case FiatAmount(amount: BigDecimal,
  * script: Script) extends CurrencyAmount Script contains Script(currency:
  * XrplCurrency, issuer: AccountAddress). Now we are getting toString for
  * currency and the value is not being quoted from a BigDecimal.
  */
opaque type XrplCurrency = BitVector

object XrplCurrency {

  private val log              = LoggerFactory.getLogger
  // This is 136 + 3 ISO char which is 24 bits (ASCII), for a total of 160
  private val low8: BitVector  = BitVector.low(8)
  private val low88: BitVector = BitVector.low(88)
  private val low40: BitVector = BitVector.low(40)

  private val validIsoChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "0123456789" +
    "<>(){}[]|?!@#$%^&*"

  def validateIsoStr(s: String): ValidatedNec[String, String] = {

    val correctLength = Validated.condNec(s.length == 3, (), s"Invalid currency format $s is not exactly 3 chars")
    val validChars    = Validated.condNec(s.forall(c => validIsoChars.contains(c)),
                                       (),
                                       s"Invalid currency format $s contains non-std characters",
                                      )
    (correctLength, validChars).mapN((_, _) => s)
  }

  /**
    * We have two formats, ISO Chars or Free Form. These are essentially the
    * same but encoded differently. Check to see which one it is, if it starts
    * with 00 then its ISO String style.. But, check is aside from first two
    * bits its ISO style and write it out that way instead. A bit of a mess that
    * is better solves with XrplCustomCurrency and XrpIsoCurrency subtypes
    */
  def toBinaryCurrencyOrIsoCurrency(xrplCurrency: XrplCurrency) = {
    // TODO
    // if first byte is non-zero set it to zero and see if it matches an ISO format.
    // if so, write out the ISO format (which is just zero the first two bytes)
    // Look at the scodec too. [[
  }

  given fsb: FixedSizeBinary[XrplCurrency](160L) with
    def fromBits(bits: BitVector): XrplCurrency = bits
    def toBits(a: XrplCurrency): BitVector      = a

  val encoder: Encoder[XrplCurrency] = Encoder.instance[XrplCurrency] { (c: XrplCurrency) =>
    log.trace(s"Encoding XrplCurrency $c")
    // Check if it just has a XRPL three char currency code or its full on random
    if c.startsWith(hex"00".bits) then
      // Its an ISO format, output as three character ASCII ISO code
      val isoBits: BitVector = c.slice(96, 96 + 24)
      log.trace(s"Encoding ISO $isoBits")
      isoBits.decodeAscii match
        case Left(err)  =>
          log.error(s"Could not decode XrplCurrency as ISO ASCII ${isoBits.toHex}:: ${err.getMessage}")
          throw new IllegalArgumentException(s"Bits Not Valid ISO Code: ${isoBits.toHex}", err)
        case Right(str) => Json.fromString(str)
    else
      // Its a custom format, output as Hex String
      Json.fromString(fsb.convertToHex(c))
  }

  /**
    * First gets the String at cursor. If its 3 char or less treats as ISO, if
    * its 160 bits (160/8 chars then custom hex
    */
  val decoder: Decoder[XrplCurrency] = Decoder.instance[XrplCurrency] { (cursor: HCursor) =>
    cursor.as[String].flatMap {
      case custom if custom.length == 20 =>
        BitVector.fromHexDescriptive(custom).leftMap((err: String) => DecodingFailure.apply(err, cursor.history))

      case iso if iso.length == 3 =>
        Try(fromIsoLikeString(iso))
          .toEither.leftMap((err: Throwable) => DecodingFailure.fromThrowable(err, cursor.history))

    }
  }

  given codec: Codec[XrplCurrency] = Codec.from(decoder, encoder)

  def fromIsoLikeString(isoLike: String): XrplCurrency = {
    log.trace(s"Creating ISO Like ${isoLike}")
    require(isoLike.length == 3, s"Invalid currency format $isoLike is not exactly 3 chars")
    require(isoLike.forall(c => validIsoChars.contains(c)),
            s"Invalid currency format $isoLike contains non-std characters",
           )
    val currency: BitVector = BitVector.encodeString(isoLike)(using StandardCharsets.US_ASCII) match
      case Left(err: CharacterCodingException) => throw new IllegalArgumentException(s"Encoding $isoLike currency", err)
      case Right(value)                        => value

    val big: BitVector = low8 ++ low88 ++ currency ++ low40
    big
  }

  /** Convertors and Helpers */
  extension (xrpCurrency: XrplCurrency)
    def asBitVector: BitVector = xrpCurrency
    def toIsoString: String    = {
      log.info(s"Raw Hex: ${xrpCurrency}")
      val alphaBytes = xrpCurrency.drop(8).drop(88).take(24)
      log.info(s"Converting XrplCurrency $alphaBytes to String")
      String(alphaBytes.toByteArray, StandardCharsets.US_ASCII)
    }
    def isIso: Boolean         = xrpCurrency(0)

}
