package com.odenzo.xrpl.models.internal

import com.odenzo.xrpl.models.api.commands.WalletPropose
import com.odenzo.xrpl.models.data.atoms.AccountAddress.*
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, AccountId }
import com.odenzo.xrpl.models.data.keys.XrpPublicKey.given
import com.odenzo.xrpl.models.data.keys.{ KeyType, XrpPublicKey, XrpSeed }
import com.tersesystems.blindsight.LoggerFactory
import io.circe.{ Codec, Decoder, Json }
import io.scalaland.chimney.PartialTransformer
import io.scalaland.chimney.partial.Result
import scodec.bits.hex

/**
  * A Wallet holds credentials and addresses for an account. It can be used to
  * sign txn etc. Normally a wallet is (initially) populated from a
  * WalletPropse.Rs
  */
case class Wallet(
    accountAddress: AccountAddress,
    keyType: KeyType,
    masterSeed: XrpSeed,
    publicKey: XrpPublicKey,
) derives Codec.AsObject {
  lazy val accountId: AccountId = accountAddress.asAccountId
}

//val GENESIS_MASTER_PASSPHRASE: String = "masterpassphrase")

object Wallet {

  private val log = LoggerFactory.getLogger

  private def decodeStringUnsafe[T: Decoder](value: String): T = Json.fromString(value).as[T] match
    case Left(value)  => throw value
    case Right(value) => value

  final val GENESIS: Wallet = Wallet(
    AccountAddress.fromAccountAddressB58Unsafe("rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh"),
    KeyType.secp256k1,
    masterSeed = decodeStringUnsafe[XrpSeed]("snoPBrXtMeMyMHUVTgbuqAfg1SUTb"),
    publicKey  = XrpPublicKey.fromBytesUnsafe(
      hex"0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020"
    ),
  )

  given PartialTransformer[WalletPropose.Rs, Wallet] =
    PartialTransformer.define[WalletPropose.Rs, Wallet].withFieldRenamed(_.accountId, _.accountAddress).buildTransformer

  def fromProposedWallet(proposed: WalletPropose.Rs): Result[Wallet] = {
    import io.scalaland.chimney.dsl.*
    proposed.transformIntoPartial[Wallet]
  }

  def fromProposedWalletUnsafe(proposed: WalletPropose.Rs): Wallet = fromProposedWallet(proposed) match
    case Result.Value(value)   => value
    case Result.Errors(errors) =>
      log.error(s"Transforming WalletPropose.Rs ${proposed} to Wallet Failed: $errors")
      throw IllegalArgumentException(s"Invalid WalletPropose to Wallet Failed $proposed -->$errors")

}
