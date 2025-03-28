package com.odenzo.xrpl.models.data.models.monetary

import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.syntax.*
import io.circe.{ Decoder, Encoder }

import java.text.DecimalFormat
import scala.util.Try

/**
  * https://xrpl.org/docs/references/protocol/binary-format#amount-fields My
  * Jack-ass way not is Going to JSON to Circe Decoding back then Scodec
  * Encoding to Bits. 555 Switch to dependant types soon
  */
enum CurrencyAmount {
  case FiatAmount(amount: FiatValue, currency: XrplCurrency, issuer: AccountAddress) extends CurrencyAmount
  case Drops(amount: Long) extends CurrencyAmount
}

object CurrencyAmount {

  extension (fa: FiatAmount) def withValue(fiatValue: FiatValue): FiatAmount = fa.copy(amount = fiatValue)
  extension (fa: FiatAmount) def withValue(fiatValue: String): FiatAmount    = fa.copy(amount = FiatValue(fiatValue))

  def drops(l: Long): CurrencyAmount.Drops = Drops(l)
  def xrp(l: Long): CurrencyAmount.Drops   = dropOps.fromXrp(l)
  def xrp(s: String): CurrencyAmount.Drops = xrp(s.toLong)
  def xrp(d: Double): CurrencyAmount.Drops = {
    val drops: Long = Math.floor(d / dropOps.dropsPerXRP.toDouble).toLong
    CurrencyAmount.Drops(drops)
  }

  def fiat(amt: String, script: Script): CurrencyAmount = FiatAmount(FiatValue(amt), script.currency, script.issuer)

  // So, those Drops. Mostly in Strings, EXCEPT the LedgerNodes (FeeSettingsNode in particular)
  // Might be wrong about that, except for ReferenceFeeUnits
  //
  // Decoder.decodeJsonNumber will handle both, not sure fn of decodeBigInt, check it
  private val stringDrops: Decoder[CurrencyAmount.Drops] = Decoder.decodeString.emapTry(rs => Try { Drops(rs.toLong) })
  private val longDrops: Decoder[CurrencyAmount.Drops]   = Decoder.decodeLong.map(l => Drops(l))
  given Decoder[CurrencyAmount.Drops]                    = stringDrops.or(longDrops)
  given Encoder[CurrencyAmount.Drops]                    = io.circe.Encoder.encodeString.contramap(drops => drops.amount.toString)

  import XrplCurrency.given

  // In this case we want to overwrite the AccountId to encode/decode json in Base24 format.
  // This needs to be aligned with the use of AccountID and AccountAddress scodec binary serialization codecs.
  given Decoder[FiatAmount] =
    Decoder.forProduct3[FiatAmount, FiatValue, XrplCurrency, AccountAddress]("value", "currency", "issuer")(
      (v, c, aa) => FiatAmount(v, c, aa)
    )

  given Encoder[FiatAmount] =
    Encoder.forProduct3("value", "currency", "issuer")(fa =>
      (fa.amount.toString(), fa.currency.asJson, fa.issuer.asJson)
    )

  given Encoder[CurrencyAmount] = Encoder.instance[CurrencyAmount] {
    case v: FiatAmount => v.asJson
    case v: Drops      => v.asJson
  }

  given Decoder[CurrencyAmount] = {
    val wideDrops: Decoder[CurrencyAmount] = stringDrops.map(v => v: CurrencyAmount)
    val wideFiat: Decoder[CurrencyAmount]  = Decoder[FiatAmount].map(v => v: CurrencyAmount)
    wideDrops.or(wideFiat)
  }

  extension (d: Drops) def asXrp: Double       = dropOps.toXrp(d)
  extension (a: FiatAmount)
    def format(bigDecimal: BigDecimal): String = new DecimalFormat("#,##0.00").format(bigDecimal)

  /**
    * Monetary amount with a given script. Script has to be an issuer, not XRP.
    * So there is no empty to define a monoid but can define a semigroup
    */

}

object dropOps {
  final val dropsPerXRP: Long  = 1000000L
  final val zero: Drops        = CurrencyAmount.Drops(0)
  final val maxDrops: BigInt   = BigInt(10).pow(17)
  final val maxXrp: BigDecimal = BigDecimal.exact(maxDrops / dropsPerXRP)

  //    /** Converts to full amount field in binary including header bits */
  //    def asByteVector(amt: Drops): ByteVector = {
  //      require(amt.amount >= 0, s"Negative amount for Drops: $amt")
  //      val bin =
  //        ByteVector.fromLong(amt.amount).or(hex"0x4000000000000000") // Make it start with bin"01". Might have to pad
  //      assert(bin.size == 64)
  //      bin
  //    }
  //
  //    def fromByteVector(bv: ByteVector): CurrencyAmount.Drops =
  //      val bits = bv.bits
  //      require(bits.size == 64, "XRP Bits must be 64 bits")
  //      require(bits.take(2) == bin"01", "XRP Bits must start with 01")
  //      CurrencyAmount.Drops(bv.toLong(false))

  /**
    * We could String encode the Drops but just going to use long for encoder.
    * Maybe need String for Decoding some responses.
    */
  given Decoder[Drops] = Decoder[Long].map(CurrencyAmount.Drops(_))

  given Encoder[Drops] = Encoder.encodeLong.contramap[Drops](_.amount)

  def fromXrp(s: String): Drops = fromXrp(BigDecimal.exact(s))

  def fromXrp(d: BigDecimal): Drops =
    require(d <= maxXrp, "sXRP Limit must be less than $maxXrp, was $d")
    Drops((d * dropsPerXRP).toLong)

  /** Use some better maths (eg BigDecimal?) */
  def toXrp(d: Drops): Double = d.amount.toDouble / dropsPerXRP.toDouble

}
