package com.odenzo.xrpl.communication

import io.circe.Json

enum CommsError(message: String) extends Throwable(message) {
  case ErrorResponse(error: String, errorCode: Int, errorMessage: String, rs: Json)
      extends CommsError("XRPL Response Error")

}
