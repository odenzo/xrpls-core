package com.odenzo.xrpl.models.api.commands

import cats.syntax.all.*
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpCommand, XrpCommandRq, XrpCommandRs }
import com.odenzo.xrpl.models.api.commands.GatewayBalances.{ Rq, Rs }
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, LedgerHash }
import com.odenzo.xrpl.models.data.ledgerids.LedgerHandle
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Used to calculate Gateway balances by excluding certain accounts (hot
  * wallets) Good way to see what issuers have issued basically.
  * https://ripple.com/build/rippled-apis/#gateway-balances TODO: Only partially
  * implemented on response side, experiment and see what the Objects arr
  */
object GatewayBalances extends XrpCommand[Rq, Rs] {

  case class Rq(
      account: AccountAddress,
      strict: Boolean                   = true,
      hotwallet: Seq[AccountAddress]    = Seq.empty[AccountAddress],
      ledgerIndex: Option[LedgerHandle] = LedgerHandle.validated.some,
      ledgerHash: Option[LedgerHash]    = None,
  ) extends XrpCommandRq derives ConfiguredCodec {
    val command: Command = Command.GATEWAY_BALANCES
  }
  object Rq:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
  // TODO: Gateway Balances Models
  case class Rs(
      account: AccountAddress,
      obligations: Option[Json],
      balances: Option[Json],
      assets: Option[Json],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
