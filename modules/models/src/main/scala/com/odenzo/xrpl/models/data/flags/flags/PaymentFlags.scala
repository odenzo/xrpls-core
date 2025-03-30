package com.odenzo.xrpl.models.data.flags.flags

import scodec.bits.{ ByteVector, hex }

//// Really UInt32 I think. And triming of the Hex leading 0 happens. Make sure to padLeft(<totalSie>) 2 or 4 ?
// Need a link to wear these are actually defined
// https://github.com/XRPLF/xrpl.js/blob/main/packages/ripple-binary-codec/src/enums/definitions.json ?
enum PaymentFlags(val v: ByteVector):
  case tfFullyCanonicalSig extends PaymentFlags(hex"80000000") // Duped?

  /** Use explicitly included paths only */
  case tfNoDirectRipple extends PaymentFlags(hex"00010000")

  /** Fail txn if can't send the full amount if disabled */
  case tfPartialPayment extends PaymentFlags(hex"00020000")

  /** Enforce input/output quality ratios on txn */
  case tfLimitQuality extends PaymentFlags(hex"00040000")
