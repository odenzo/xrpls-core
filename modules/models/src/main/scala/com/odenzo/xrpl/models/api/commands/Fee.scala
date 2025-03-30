package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.Drops
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Get the current XRPL server Fee statistics.
  * https://ripple.com/build/rippled-apis/#server-info
  */
object Fee extends XrpCommand[Fee.Rq, Fee.Rs] {

  

  /** Case Object that is empty? */
  case class Rq() extends XrpCommandRq derives ConfiguredCodec {
    def command: Command = Command.FEE
  }

  // TODO: Move the model classes out to main tree (our of commands)
  case class FeeAmounts(baseFee: Drops, medianFee: Drops, minimumFee: Drops, openLedgerFee: Drops)
      derives ConfiguredCodec

  case class FeeLevels(
      medianLevel: BigDecimal,
      minimumLevel: BigDecimal,
      openLedgerLevel: BigDecimal,
      referenceLevel: BigDecimal,
  ) derives ConfiguredCodec

  case class Rs(
      currentQueueSize: Int,
      drops: FeeAmounts,
      currentLedgerSize: Long,
      expectedLedgerSize: Long,
      ledgerCurrentIndex: LedgerIndex, // Not LedgerSequence?
      levels: FeeLevels,
      maxQueueSize: Long,
  ) extends XrpCommandRs derives ConfiguredCodec

}
