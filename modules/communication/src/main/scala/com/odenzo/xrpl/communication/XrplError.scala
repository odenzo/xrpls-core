package com.odenzo.xrpl.communication

import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Note that this differs between RPC error responses and WS responses? Or we
  * get the error and lookup in reference table the error code and error
  * message?
  */
case class XrplError(error: String, errorCode: Option[Long], errorMessage: Option[String]) derives ConfiguredCodec

object XrplError:
  given Configuration = Configuration.default.withSnakeCaseConstructorNames
