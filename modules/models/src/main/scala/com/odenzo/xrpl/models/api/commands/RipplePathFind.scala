package com.odenzo.xrpl.models.api.commands

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.data.ledgertree.nodes.OfferNode
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.{ LedgerIndex, validated }
import com.odenzo.xrpl.models.data.models.monetary.{ BookCurrency, CurrencyAmount, Script, XrplCurrency }
import com.odenzo.xrpl.models.data.models.paths.XrplPossiblePaths
import io.circe.Json
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/** This is the  simple version of path find, RPC and WS supported. */
object RipplePathFind extends XrpCommand[RipplePathFind.Rq, RipplePathFind.Rs] {

  case class Rq(
      ledgerIndex: Option[LedgerHandle]                    = LedgerHandle.validated.some,
      ledgerHash: Option[LedgerHash]                       = None,
      sourceAccount: AccountAddress,
      sourceCurrencies: Option[NonEmptyList[XrplCurrency]] = None, // Each currency encoded as Object
      destinationAccount: AccountAddress,
      destinationAmount: CurrencyAmount, // TODO: Test -1 for XRP or FiatCurrency Amount
      sendMax: Option[CurrencyAmount]                      = None,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.RIPPLE_PATH_FIND
  }
  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Rs(
      alternative: List[XrplPossiblePaths],
      destinationAccount: AccountAddress,
      destinationCurrencies: List[XrplCurrency],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames

}
