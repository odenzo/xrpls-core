package com.odenzo.xrpl.models.api.commands

import com.odenzo.xrpl.models.api.commands.Command
import com.odenzo.xrpl.models.api.commands.CommandMarkers.{ XrpAdminCommandRq, XrpCommand, XrpCommandRs }
import com.odenzo.xrpl.models.data.models.atoms.*
import com.odenzo.xrpl.models.data.models.keys.{ KeyType, XrpPublicKey, XrpSeed }
import io.circe.derivation.*

/**
  * https://xrpl.org/docs/references/http-websocket-apis/admin-api-methods/key-generation-methods/wallet_propose
  * This seems to NOT like duplicate seed / passphrase fields even if one is
  * null. So need to drop null values
  */
object WalletPropose extends XrpCommand[WalletPropose.Rq, WalletPropose.Rs] {
  import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.Codecs.given

  /**
    * @param keyType
    *   ed25519 or secp256k1
    * @param seed
    * @param passphrase
    * @param command
    */
  case class Rq(
      seed: Option[XrpSeed],
      passphrase: Option[String],
      keyType: KeyType,
  ) extends XrpAdminCommandRq derives ConfiguredCodec {
    def command: Command = Command.WALLET_PROPOSE
  }

  object Rq:
    given Configuration  = Configuration.default.withSnakeCaseMemberNames
    val command: Command = Command.WALLET_PROPOSE

    // val encRpc: Encoder.AsObject[Rq] = summon[Encoder.AsObject[Rq]].mapJsonObject(commandField)

  /**
    * @param keyType
    *   Will be enum when bring in full models
    * @param accountAddress
    *   Base58
    * @param masterSeed
    *   Base58
    * @param publicKey
    *   Base58
    * @param warning
    *   If insecure seed
    */
  case class Rs(
      accountId: AccountAddress,
      keyType: KeyType,
      masterKey: String,
      masterSeed: XrpSeed,
      masterSeedHex: String,
      publicKey: XrpPublicKey,
      publicKeyHex: String, // Would normally omit the hex, since standard decoder is Base58
      warning: Option[String],
  ) extends XrpCommandRs derives ConfiguredCodec

  object Rs:
    given Configuration = Configuration.default.withSnakeCaseMemberNames
}
