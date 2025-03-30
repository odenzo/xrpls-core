package com.odenzo.xrpl.models.data.monetary

import scodec.bits.{ ByteVector, hex }
import scodec.codecs.utf8

object CurrencyAmountConstants {

  /**
    * Maximum XRP amount expressed in Drops, which requires only 7 bytes so safe
    * in a Long
    */
  val maxDrops: BigInt = spire.math.pow(BigInt(10), BigInt(17))

  /** Packaged up zeroFiatAmount */
  val rawEncodedZeroFiatAmount: ByteVector = hex"0x80".padRight(20)

  val xrpCurrencyCode: ByteVector = hex"00".padLeft(20) // Same as ACCOUNT_ZERO

  // The raw hex form in json can be handled, but can't be XRP
  // This should be 24 bits, 'X'.ascii , 'R'.ascii, 'P'.ascii (UTF-8 equivalent in that range)
  val correctXrpHexCode: ByteVector = utf8.encode("XRP").require.bytes

  /** Standard XRP encoding, like an ISO, for when binning a XRP currency? * */
  val xrpHex: ByteVector = hex"0158415500000000C1F76FF6ECB0BAC600000000"

}
