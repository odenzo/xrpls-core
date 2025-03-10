package com.odenzo.xrpl.common.utils

import cats.*
import cats.data.*
import cats.syntax.all.*
import scodec.bits.*
import scodec.codecs.*
import spire.*
import spire.implicits.*
import spire.math.*
import spire.syntax.*

import java.util.Locale
import scala.util.Try

/**
  * Some combination of Spire and Scodec Codecs to Handle Unsigned Numbers
  * Primarily
  */
trait ByteUtils {
  val bytezero: Byte = 0.toByte

}

object ByteUtils extends ByteUtils
