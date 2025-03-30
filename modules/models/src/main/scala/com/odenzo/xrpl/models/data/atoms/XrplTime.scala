package com.odenzo.xrpl.models.data.atoms

import io.circe.{ Decoder, Encoder }

import java.time.Instant

/**
  * The rippled server and its APIs represent time as an unsigned integer. This
  * number measures the number of seconds since the "Ripple Epoch" of January 1,
  * 2000 (00:00 UTC). This is like the way the Unix epoch works, except the
  * Ripple Epoch is 946684800 seconds after the Unix Epoch. And the resolution
  * is ONE SECOND, doh. Don't convert Ripple Epoch times to UNIX Epoch times in
  * 32-bit variables: this could lead to integer overflows.
  */

case class XrplTime(ticks: Long) extends AnyVal {
  def asInstant: Instant             = Instant.ofEpochSecond(ticks + XrplTime.rippleOffset)
  def asISO: String                  = asInstant.toString
  def plusSeconds(l: Long): XrplTime = XrplTime(this.ticks + l) // TODO: Check for overflow
  override def toString: String      = s"$asISO ($ticks)"

}

object XrplTime {

  final private val rippleOffset: Long  = 946684800L
  def now: XrplTime                     = fromInstant(Instant.now())
  def fromInstant(i: Instant): XrplTime = new XrplTime(i.getEpochSecond - XrplTime.rippleOffset)

  given encoder: Encoder[XrplTime] = Encoder.encodeLong.contramap[XrplTime](_.ticks)
  given decoder: Decoder[XrplTime] = Decoder.decodeLong.map(XrplTime(_))
}
