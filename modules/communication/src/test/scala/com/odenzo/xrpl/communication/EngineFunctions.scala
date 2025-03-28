package com.odenzo.xrpl.communication

import cats.effect.IO
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommandRq, XrpCommandRs }

/** Just a typedef since I can't fgure out how to make an opqaue type. */
object EngineFunctions {
  opaque type SendCmdFn[T <: XrpCommandRq, R <: XrpCommandRs] = T => IO[R]

  object SendCmdFn {
    def apply[T <: XrpCommandRq, R <: XrpCommandRs](fn: T => IO[R]): SendCmdFn[T, R] = fn

    /** Extension methods for SendCmdFn */
    extension [T <: XrpCommandRq, R <: XrpCommandRs](sendCmdFn: SendCmdFn[T, R]) {

      /**
        * This allows you to call the SendCmdFn like a normal function. Example:
        * val result: IO[R] = sendCmdFn(myRequest)
        */
      def apply(rq: T): IO[R] = sendCmdFn(rq)
    }
  }
}
