package com.odenzo.xrpl.models.data.models.flags

import io.circe.{ Decoder, Encoder }
import scodec.bits.{ BitVector, ByteVector, hex }

import scala.collection.immutable

/**
  * This is the flag in the PaymentTx, may be used other places. They are 42
  * bit-wise flags and masked based on context. Should we have a Tag for Flags
  * by type. Is that possible even with opaque type, I can't figure it out.
  *
  * The PaymentFlags etc in flags package can be ordered into here. I *think*
  * all flags are 32-bit so this is a BinaryFixedSize and we could add some
  * BitWise operations to another typeclass. Really all we want to do is
  * immutable AND that I can think of. Not that AccountFlags are special, and
  * are not bitwise, see them for docs.
  */
opaque type Flags = BitVector

object Flags {
  val zero: BitVector              = BitVector.low(32)
  def unsafe(bv: BitVector): Flags = bv
  def fromLong(l: Long): Flags     = BitVector.fromLong(l, 32)

  val universalFlagMask  = hex"ff000000"
  val typeBasedFlagsMask = hex"00ff0000"
  val reservedFlagsMask  = hex"0000ffff"

  extension (flag: Flags)
    def bits: BitVector    = flag
    def unsignedLong: Long = bits.toLong(false)

    /** Set a bitflag by anding. */
    def and(b: BitVector): Flags = flag.and(b)

    /** CLear from a bitvector with one bit set normally (can be multiple) */
    def xor(b: BitVector): Flags = flag.xor(b)

  /** Not sure we are going to have different ways of encodering/decoding */
  given Encoder[Flags] = Encoder.encodeLong.contramap[Flags] { flags => flags.unsignedLong }
  given Decoder[Flags] = Decoder.decodeLong.map(l => Flags.fromLong(l))
}

/**
  * A Flag Represent a Single Value, may be Integral value or just a single bit
  */
opaque type BitFlag = BitVector

/**
  * Bit flag represented as a long with no enforcement except convention that
  * only a single bit set. We let calculations and storage allow multiple bits
  * set.
  * @param value
  */
object BitFlag:
  def combine(a: BitFlag, b: BitFlag): BitVector = a | b

///**
//  * Quote: https://ripple.com/build/transactions/#flags The Flags field can
//  * contain various options that affect how a transaction should behave. The
//  * options are represented as binary values that can be combined with
//  * bitwise-or operations to set multiple flags at once.
//  *
//  * Most flags only have meaning for a specific transaction type. The same
//  * bitwise value may be reused for flags on different transaction types, so it
//  * is important to pay attention to the TransactionType field when setting and
//  * reading flags. This overlap trust set flag, not sure about values. Keep
//  * seperate since not isomorphic. Payment Transactions support these
//  * *additional* flags (additional to other tfFlags maybe?)
//  */
//
//// Really UInt32 I think. And triming of the Hex leading 0 happens. Make sure to padLeft(<totalSie>) 2 or 4 ?
// Need a link to wear these are actually defined
// https://github.com/XRPLF/xrpl.js/blob/main/packages/ripple-binary-codec/src/enums/definitions.json ?
enum PaymentFlag(val v: ByteVector):
  case tfFullyCanonicalSig extends PaymentFlag(hex"80000000") // Duped?

  /** Use explicitly included paths only */
  case tfNoDirectRipple extends PaymentFlag(hex"00010000")

  /** Fail txn if can't send the full amount if disabled */
  case tfPartialPayment extends PaymentFlag(hex"00020000")

  /** Enforce input/output quality ratios on txn */
  case tfLimitQuality extends PaymentFlag(hex"00040000")

///**
//  * AccountFlags used (mostly/only?) in AccountSet. Not these are <em>not
//  * bit-mask kind of flags</em> aka AccountSetFlags
//  */
//enum AccountFlag(val value: Long):
//
//  case asfNOOP extends AccountFlag(0)
//
//  /** Require a destination tag to send txn to this account. */
//  case asfRequireDest extends AccountFlag(1)
//
//  /** Require authorization for users to hold balances issued */
//  case asfRequireAuth extends AccountFlag(2)
//
//  /** XRP should not be sent to this account. */
//  case asfDisallowXRP extends AccountFlag(3)
//
//  /** Disallow use of the master key */
//  case asfDisableMaster extends AccountFlag(4)
//
//  /** Track the ID of this account's most recent txn */
//  case asfAccountTxnID extends AccountFlag(5)
//
//  /**
//    * Permanently give up the ability to freeze trust lines or disable Global
//    * Freeze
//    */
//  case asfNoFreeze extends AccountFlag(6)
//
//  /** "Freeze all assets issued by this account */
//  case asfGlobalFreeze extends AccountFlag(7)
//
//  /** Enable rippling on this account's trust lines by default */
//  case asfDefaultRipple extends AccountFlag(8)
//end AccountFlag
//
enum LedgerFlag(val value: ByteVector):

  /** Indicates that the account has used its free SetRegularKey transaction. */
  case lsfPasswordSpent extends LedgerFlag(hex"00010000")

  /** Requires incoming payments to specify a Destination Tag. asfRequireDest */
  case lsfRequireDestTag extends LedgerFlag(hex"00020000")

  /**
    * This account must individually approve other users for those users to hold
    * this account's issuances asfRequireAuth
    */
  case lsfRequireAuth extends LedgerFlag(hex"00040000")

  /**
    * Client applications should not send XRP to this account. Not enforced by
    * rippled. asfDisallowXRP
    */
  case lsfDisallowXRP extends LedgerFlag(hex"00080000")

  /**
    * Disallows use of the master key to sign transactions for this account.
    * asfDisableMaster
    */
  case lsfDisableMaster extends LedgerFlag(hex"00100000")

  /**
    * This address cannot freeze trust lines connected to it. Once enabled,
    * cannot be disabled. asfNoFreeze
    */
  case lsfNoFreeze extends LedgerFlag(hex"00200000")

  /** All assets issued by this address are frozen.	asfGlobalFreeze */
  case lsfGlobalFreeze extends LedgerFlag(hex"00400000")

  /**
    * Enable rippling on this addresses's trust lines by default. Required for
    * issuing addresses; discouraged for others. asfDefaultRipple
    */
  case lsfDefaultRipple extends LedgerFlag(hex"00800000")
end LedgerFlag

/** UInt32 */
enum TrustSetFlag(val value: Int, val bits: ByteVector):

  /**
    * Authorize the other party to hold issuances from this account. (No effect
    * unless using the asfRequireAuth AccountSet flag.) Cannot be unset
    */
  case tfSetfAuth extends TrustSetFlag(65536, hex"00010000")

  /**
    * Blocks rippling between two trustlines of the same currency, if this flag
    * is set on both.(See No Ripple for details)
    */
  case tfSetNoRipple extends TrustSetFlag(131072, hex"00020000")

  /** Clears the No -Rippling flag.(See No Ripple for details. */
  case tfClearNoRipple extends TrustSetFlag(262144, hex"00040000")

  /** "Freeze the trustline." */
  case tfSetFreeze extends TrustSetFlag(1048576, hex"0100000")

  /** Unfreeze the trustline */
  case tfClearFreeze extends TrustSetFlag(2097152, hex"00200000")

  /**
    * Flags for offers. These are BitMask style flags (or'ed together)
    * https://ripple.com/build/transactions/#offercreate
    */
end TrustSetFlag

//
//enum OfferCreateFlag(val value: Int):
//
//  case tfPassive extends OfferCreateFlag(65536)
//
//  /**
//    * Never becomes a ledger node, just tries to match current existing orders
//    * or fails. (Partial fill I guess(
//    */
//  case tfImmediateOrCancel extends OfferCreateFlag(131072)
//
//  /**
//    * Same as immediate of cancel, tries to make existing offer but only if full
//    * TakerPays amount can be matched. (I think this can be matched across
//    * multiple other offers though, test)
//    */
//  case tfFillOrKill extends OfferCreateFlag(262144)
//
//  /** Sell all offered even if get more than desired result. */
//  case tfSell extends OfferCreateFlag(1048576)
