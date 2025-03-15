package com.odenzo.xrpl.communication.models

import io.circe.{ Codec, JsonObject }

/**
  * RPC Responses *may* contain an array of these in the warning field.
  * https://xrpl.org/docs/references/http-websocket-apis/api-conventions/response-formatting
  * This is for the warnings embedded in the top level envelope.
  */
case class XrplWarning(id: Int, message: String, details: Option[JsonObject]) derives Codec.AsObject
