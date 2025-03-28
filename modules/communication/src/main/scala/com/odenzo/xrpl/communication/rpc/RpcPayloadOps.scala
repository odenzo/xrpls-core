package com.odenzo.xrpl.communication.rpc

import cats.*
import cats.effect.*
import io.circe.Json
import io.circe.pointer.literal.pointer

object RpcPayloadOps {

  def extractResult(root: Json): IO[Json] = IO.fromEither(pointer"/result".get(root))

  /** Extract  Command  aka Method From a Command Request JSON */
  def extractCommand(root: Json): IO[Json] = IO.fromEither(pointer"/method".get(root))

  /** Extracts the Request Body buried in a param */
  def extractRequest(root: Json): IO[Json] = IO.fromEither(pointer"/params/0".get(root))

  def extractStatus(root: Json) = IO.fromEither(pointer"/result/status".get(root))
}
