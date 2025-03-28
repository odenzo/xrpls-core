package com.odenzo.xrpl.models.support.utils.convertors

import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.monetary.*
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.FiatAmount
import io.scalaland.chimney.Transformer
import scodec.bits.{ BitVector, ByteVector }

/**
  * Scala 3 Conversions, maybe of which are for extracting pertinent info from
  * data classes. Not sure I am going to rely more on Chimney to consistently
  * do, or for some low cost use Scala 3 The Conversion I may regret, and put
  * them here so need to be explicitly imported ( `import Conversions.given`
  */
object Conversions {

  /** Unidirection implicit conversion from BitVector to ByteVector */
  given Conversion[BitVector, ByteVector] with
    def apply(x: BitVector): ByteVector = x.bytes

  ////////////////// Chimney Convertors (1.7.1 as baseline). Combination of useful functions and some givens.
  import io.scalaland.chimney.dsl.*
  // import io.scalaland.chimney.cats.*

  given trustLine2fiatAmount: Transformer[TrustLine, CurrencyAmount.FiatAmount] = (tl: TrustLine) => {
    tl.into[FiatAmount].withFieldRenamed(_.balance, _.amount).withFieldRenamed(_.account, _.issuer).transform
  }

  def exampleForOne(tl: TrustLine): CurrencyAmount.FiatAmount = tl.transformInto[CurrencyAmount.FiatAmount]

  val ess: Script =
    FiatAmount(FiatValue("10"), XrplStdCurrency.USD.currency, AccountAddress.GENESIS).transformInto[Script]
}
