package com.odenzo.xrpl.models.api.transactions.support

import cats.syntax.all.*
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.atoms.AccountTxnNumber
import com.odenzo.xrpl.models.data.models.flags.Flags
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.memos.{ Memo, Memos }
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.derivation.{ Configuration, ConfiguredCodec }

/**
  * Some common TxJson Request Parameters This includes both single-signed and
  * multi-signed transaction. What happens if we loose hash on multi-sign
  * roundtripping?
  *
  * This is basically a base class of all the Tx but can't inherit from case
  * class which allows making the Circe Codecs much easily. This is a pain and
  * might CommonTx[Option] it, seems I need to build incrementally. Need a real
  * builder to enforce paramaters (signers xor singingPubKey type stuff
  *
  * The whole design is flawed. Lets take out Signers, and this is the base that
  * is specified prior to signing a Tx. Then functions to
  *   - go embedding a TxJson
  *   - Signing XOR multisigning
  *   - Ticketing Flow
  *   - etc etc
  * https://xrpl.org/docs/references/protocol/transactions/common-fields
  * @param lastLedgerSequence
  * @param fee
  * @param flags
  * @param sequence
  */
case class TxCommon(
                     fee: Option[CurrencyAmount.Drops]       = None, // Required but auto-fillable
                     memos: Option[Memos]                    = None,
                     sequence: Option[AccountTxnNumber]    = None,
                     lastLedgerSequence: Option[LedgerIndex] = None,
                     //    signingPubKey: Option[SigningPublicKey] = None,
                     //    signers: Option[Signers]                = None, // Multi-sig signers
                     flags: Option[Flags]                    = None,
                     sourceTag: Option[Int]                  = None, // Really a UINT32
) derives ConfiguredCodec {
  def addMemo(m: Memo): TxCommon                            = {
    val memos = this.memos match {
      case None        => Memos.fromMemo(m)
      case Some(memos) => memos.add(m)
    }
    this.copy(memos = Some(memos))
  }
  def withMemos(m: Memos): TxCommon                         = this.copy(memos = Some(m))
  def withLastLedgerSeq(ledgerIndex: LedgerIndex): TxCommon = this.copy(lastLedgerSequence = Some(ledgerIndex))
}

object TxCommon extends CirceCodecUtils {
  given Configuration = CirceCodecUtils.customConfiguration(capitalize)

  /** Need some nice factory builders */
  val default = TxCommon(fee = CurrencyAmount.Drops(555).some)
}
