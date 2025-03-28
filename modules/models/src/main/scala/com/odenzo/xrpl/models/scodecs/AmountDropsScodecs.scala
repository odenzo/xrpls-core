package com.odenzo.xrpl.models.scodecs

import com.odenzo.xrpl.models.data.models.monetary.{ CurrencyAmount, dropOps }
import com.tersesystems.blindsight.LoggerFactory
import scodec.*
import scodec.bits.*
import scodec.codecs.*

/**
  * https://xrpl.org/currency-formats.html#issued-currency-math ULong max is
  * 18,446,744,073,709,551,615 First bit is taken to indicate its not XRP.
  *
  * Well, this is a mess, because we have a few kinds of amounts. XRP amount, a
  * TokenAmount (amount + currency + issuer) and the currency can be a ISO or
  * free form Custom Currency. Note: This is not just the "numeric" amount,
  * handle it all.
  *
  *   - Stuffing a bit of this functionality down into the models. -54-bit
  *     mantissa normalized to (10^15 ,10^16-1)
  * 8-bit exponent which is math exponent encoded +97 (uint)
  */
object AmountDropsScodecs {
  import CurrencyAmount.*

  private val log                             = LoggerFactory.getLogger
  final private[scodecs] val maxDrops: BigInt = BigInt(10).pow(17)

  val xrpDropsEnc: Encoder[CurrencyAmount.Drops] = Encoder[CurrencyAmount.Drops] { (drops: CurrencyAmount.Drops) =>
    log.info(s"Runnign Drops Encoder")
    if drops.amount < 0 || drops.amount > dropOps.maxDrops
    then Attempt.failure(Err(s"Drops $drops not Range (0,$maxDrops)"))
    else ulong(62).encode(drops.amount).map(bin"01" ++ _)
  }

  val xrpDropsDecoder: Decoder[CurrencyAmount.Drops] =
    (constant(bin"01") ~> ulong(62)).withContext("DropsAmount").asDecoder.emap { (a: Long) =>
      log.info("Running Drops Decoder")
      if a > maxDrops
      then Attempt.failure(Err(s"Drops $a not Range (0,$maxDrops)"))
      else Attempt.successful(Drops(a))
    }

  given xrpDropsCodec: Codec[CurrencyAmount.Drops] = Codec(xrpDropsEnc, xrpDropsDecoder).withContext("DropsAmount")

}
