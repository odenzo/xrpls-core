package com.odenzo.xrpl.communication

import cats.effect.IO
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }
import io.circe.Encoder

object EngineFunctions {
  type SendCmdFn = [T <: XrpCommandRq, R <: XrpCommandRs] => T => IO[R]
}
