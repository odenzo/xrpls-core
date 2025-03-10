package com.odenzo.xrpl.models.scodecs

import scodec.bits.Bases.Alphabet

/** This is not happy... go back to manual or investigate. Maybe it is the dreaded r */
class RippleBase58Alphabet extends Alphabet {

  private val base58RippleAlphabet = ScodecConstants.base58RippleAlphabet

  private val Chars = base58RippleAlphabet.toIndexedSeq

  def toChar(i: Int): Char = Chars(i)

  def toIndex(c: Char): Int = Chars.indexOf(c) match {
    case -1 => throw new IllegalArgumentException
    case i  => i

  }

  def ignore(c: Char): Boolean = c.isWhitespace
}
