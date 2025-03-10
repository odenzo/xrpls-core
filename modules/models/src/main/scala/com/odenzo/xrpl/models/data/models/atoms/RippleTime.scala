package com.odenzo.xrpl.models.data.models.atoms

import java.time.Instant

import io.circe.{ Decoder, Encoder }

/**
  * The rippled server and its APIs represent time as an unsigned integer. This
  * number measures the number of seconds since the "Ripple Epoch" of January 1,
  * 2000 (00:00 UTC). This is like the way the Unix epoch works, except the
  * Ripple Epoch is 946684800 seconds after the Unix Epoch. And the resolution
  * is ONE SECOND, doh. Don't convert Ripple Epoch times to UNIX Epoch times in
  * 32-bit variables: this could lead to integer overflows.
  */

case class RippleTime(ticks: Long) extends AnyVal {
  def asInstant: Instant        = Instant.ofEpochSecond(ticks + RippleTime.rippleOffset)
  def asISO: String             = asInstant.toString
  def plusSeconds(l: Long)      = RippleTime(this.ticks + l) // TODO: Check for overflow
  override def toString: String = s"$asISO ($ticks)"

  // TODO: Validate ticks is positive
}

object RippleTime {

  final private val rippleOffset: Long    = 946684800L
  def now: RippleTime                     = fromInstant(Instant.now())
  def fromInstant(i: Instant): RippleTime = new RippleTime(i.getEpochSecond - RippleTime.rippleOffset)

  implicit val encoder: Encoder[RippleTime] = Encoder.encodeLong.contramap[RippleTime](_.ticks)
  implicit val decoder: Decoder[RippleTime] = Decoder.decodeLong.map(RippleTime(_))
}
