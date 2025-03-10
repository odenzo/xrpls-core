package com.odenzo.xrpl.models.scodecs

import io.circe.Json

enum BinCodecError(e: Throwable) extends Throwable(e):
  case GeneralError(e: Throwable) extends BinCodecError(e)
  case MalformedJson(e: Throwable) extends BinCodecError(e)
  case EncodingError(json:Json, cause: Throwable) extends BinCodecError(cause)

object BinCodecError:
  def fromString(s: String): BinCodecError = GeneralError(Throwable(s))
