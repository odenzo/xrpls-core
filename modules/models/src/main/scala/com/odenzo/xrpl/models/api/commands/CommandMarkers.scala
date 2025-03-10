package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.common.binary.XrpBinaryOps
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import io.circe.{ Decoder, Encoder, Json }

object CommandMarkers {

  trait XrpCommandRq {
    def command: Command
  }

  trait XrpCommandRs

  trait XrpAdminCommandRq extends XrpCommandRq

  /**
    * Not sure how I will use this, currently experimenting just on the Rs and
    * manually including marker in Rq/Rq. Could have one for the Rq too, and
    * insure it has limit and marker in there, but not yet.
    */
  trait XrpScrolling

  /**
    * This goes on the enclosing object that has the Rq and Rs inside it, to
    * bring in common utility functions. This can be used to add Circe Helpers
    * to perhaps.
    */
  trait XrpCommand[RQ <: XrpCommandRq: Encoder: Decoder, RS <: XrpCommandRs: Encoder.AsObject: Decoder]
      extends XrpBinaryOps { self =>

  }
}
