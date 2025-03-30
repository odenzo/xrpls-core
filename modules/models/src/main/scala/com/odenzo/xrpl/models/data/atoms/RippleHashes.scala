package com.odenzo.xrpl.models.data.atoms

import cats.implicits.*
import com.odenzo.xrpl.common.binary.FixedSizeBinary
import com.odenzo.xrpl.models.data.atoms.hash256.*
import io.circe.{ Codec, Decoder, Encoder }

/**
  * This object has wrapper classes around Hash256 and Hash160 opaque types.
  * This allows FixedSizeBinary typeclass to be used. Opaque types have their
  * drawbacks.
  */
object RippleHashes {

  given codecHash256: Codec[Hash256] = summon[FixedSizeBinary[Hash256]].hexCodec

  case class PaymentChannelHash(v: Hash256) derives Codec

  /**
    * Represents basic datatype for Ripple Hashes. There are a few types. This
    * is my poor design. Outside this file can embedd RippleHash Really is
    * generic hashses, and LedgerHash embedds is, but TxnHash extends Hash256
    * directly. meh.... warts and all for now.
    *
    * @see
    *   [[https://ripple.com/build/rippled-apis/#hashes]]
    */

  /**
    * A Hash for a Transaction that can be used for TxRq to lookup Transaction
    * e.g. 5DE3F4F3558EA2A278576E6E59ABA88A697A3BE3C97E017D590743D88F366C4D
    */
  case class TxnHash(v: Hash256)

  /**
    * Invoice Hash is a specific field on each transaction, similar to a memo
    * fields. Used for chargebacks and reversals as pattern at semantic layer.
    * Don't see anywhere to search by this, which would be nice.
    *
    * @param v
    */
  case class InvoiceHash(v: Hash256)

  /** For Payment Channels */
  case class ChannelIndex(v: Hash256)

  /** TODO: Check on this, is it really AccountLedger ID */
  case class AccountHash(v: Hash256) extends AnyVal

  object TxnHash {
    given Codec[TxnHash] = codecHash256.iemap[TxnHash](h => this(h).asRight)(m => m.v)
  }

  object ChannelIndex {
    given Codec[ChannelIndex] = codecHash256.iemap[ChannelIndex](h => this(h).asRight)(m => m.v)
  }

  object AccountHash {
    given Codec[AccountHash] = codecHash256.iemap[AccountHash](h => this(h).asRight)(m => m.v)

  }

  object InvoiceHash {
    given Codec[InvoiceHash] = codecHash256.iemap[InvoiceHash](h => this(h).asRight)(m => m.v)

  }

}
