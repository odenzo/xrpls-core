package com.odenzo.xrpl.models.data.models.flags.flags

import io.circe.{ Decoder, Encoder }
import scodec.bits.{ BitVector, ByteVector, hex }

import scala.collection.immutable
import cats.effect.*
import cats.effect.syntax.all.*

import cats.*
import cats.data.*
import cats.syntax.all.*

/**
  * AccountFlags used (mostly/only?) in AccountSet. Not these are <em>not
  * bit-mask kind of flags</em> aka AccountSetFlags
  */
enum AccountFlag(val value: Long):

  case asfNOOP extends AccountFlag(0)

  /** Require a destination tag to send txn to this account. */
  case asfRequireDest extends AccountFlag(1)

  /** Require authorization for users to hold balances issued */
  case asfRequireAuth extends AccountFlag(2)

  /** XRP should not be sent to this account. */
  case asfDisallowXRP extends AccountFlag(3)

  /** Disallow use of the master key */
  case asfDisableMaster extends AccountFlag(4)

  /** Track the ID of this account's most recent txn */
  case asfAccountTxnID extends AccountFlag(5)

  /**
    * Permanently give up the ability to freeze trust lines or disable Global
    * Freeze
    */
  case asfNoFreeze extends AccountFlag(6)

  /** "Freeze all assets issued by this account */
  case asfGlobalFreeze extends AccountFlag(7)

  /** Enable rippling on this account's trust lines by default */
  case asfDefaultRipple extends AccountFlag(8)
end AccountFlag

object AccountFlag:
  // I wonder if I can use Singleton types of the Int value with a trait, instead of some lookup
  given Encoder[AccountFlag] = Encoder.encodeLong.contramap(_.value)
  given Decoder[AccountFlag] = Decoder.decodeLong.emap { l =>
    AccountFlag.values.find(_.value == l).toRight(s"Invalid AccountFlag $l")
  }
