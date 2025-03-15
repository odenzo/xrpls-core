package com.odenzo.xrpl.communication.models

import io.circe.{ Encoder, Json }

case class RpcRequest(method: String, params: List[Json], api_version: Int = 2) derives Encoder.AsObject
