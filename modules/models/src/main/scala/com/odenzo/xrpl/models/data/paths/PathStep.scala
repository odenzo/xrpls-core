package com.odenzo.xrpl.models.data.paths

import com.odenzo.xrpl.models.data.atoms.AccountAddress
import io.circe
import io.circe.*
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.generic.semiauto.*

import java.util.Currency

trait PathStep

object FiatPathStep:
  given Configuration = Configuration.default.withSnakeCaseMemberNames

/**
  * Several different types of nodes. type = 1 is just account , type 48 is
  * currency and issuer
  *
  * @param currency
  * @param issuer
  * @param zType
  * @param type_hex
  */
case class FiatPathStep(currency: Currency, issuer: Option[AccountAddress], `type`: Int, type_hex: String)
    extends PathStep derives ConfiguredCodec {
  val pathType: Int = `type`
}

object XrpPathStep:
  given Configuration = Configuration.default.withSnakeCaseMemberNames

case class XrpPathStep(account: AccountAddress, `type`: Int, typeHex: String) extends PathStep derives ConfiguredCodec {
  val pathType: Int = `type`
}
