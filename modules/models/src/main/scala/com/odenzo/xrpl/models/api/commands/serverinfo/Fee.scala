package com.odenzo.xrpl.models.api.commands.serverinfo

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.syntax.*

/**
  * Get the current XRPL server Fee statistics.
  * https://ripple.com/build/rippled-apis/#server-info
  */
object Fee extends XrpCommand[Fee.Rq, Fee.Rs] {


  /** Case Object that is empty? */
  case class Rq() extends XrpCommandRq derives ConfiguredCodec {
    def command: Command = Command.FEE
  }

  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  // TODO: Move the model classes out to main tree (our of commands)
  case class FeeAmounts(baseFee: Drops, medianFee: Drops, minimumFee: Drops, openLedgerFee: Drops)
      derives ConfiguredCodec

  object FeeAmounts:
    given Configuration = Configuration.default.withSnakeCaseMemberNames


  case class FeeLevels(
      medianLevel: BigDecimal,
      minimumLevel: BigDecimal,
      openLedgerLevel: BigDecimal,
      referenceLevel: BigDecimal,
  ) derives ConfiguredCodec

  object FeeLevels:
    given Configuration = Configuration.default.withSnakeCaseMemberNames


  case class Rs(
      currentQueueSize: Int,
      drops: FeeAmounts,
      currentLedgerSize: Long,
      expectedLedgerSize: Long,
      ledgerCurrentIndex: LedgerIndex, // Not LedgerSequence?
      levels: FeeLevels,
      maxQueueSize: Long,
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
