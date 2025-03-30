package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object CanDelete extends XrpCommand[CanDelete.Rq, CanDelete.Rs] {

  /**
    * https://ripple.com/build/rippled-apis/#can-delete This just takes
    * ledgerindex now. For "never" use LedgerIndex.MIN (LedgerIndex(0)) For
    * "always" use LedgerIndex.MAX LedgerIndex(4294967295) This will give an
    * error if online_delete is not enabled on the server (which it isn't for my
    * testnets :-()
    *
    * @param canDelete
    * @param id
    */
  case class Rq(canDelete: LedgerIndex = LedgerHandle.MIN) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.CAN_DELETE
  }

  case class Rs(canDdelete: LedgerIndex) extends XrpCommandRs derives ConfiguredCodec

  object Rq {
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  }

  object Rs {
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  }

}
