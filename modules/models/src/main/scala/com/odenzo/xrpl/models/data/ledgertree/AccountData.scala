package com.odenzo.xrpl.models.data.ledgertree

import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.common.utils.CirceCodecUtils.capitalize
import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.TxnHash
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, AccountTxnId, AccountTxnNumber }
import com.odenzo.xrpl.models.data.models.flags.Flags
import com.odenzo.xrpl.models.data.models.ledgerids.LedgerHandle.LedgerIndex
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount
import io.circe.derivation.{ Configuration, ConfiguredCodec }
import io.circe.{ Decoder, Json }

/**
  * The `account_info` inquiry returns this variant of account root ledger under
  * account_data Should be merged with AccountRootEntry , this has
  * (additionally?) signer_lists and probably the queue_data (optional in
  * AccountInfo call) https://ripple.com/build/ledger-format/#accountroot We
  * just take a subset of data now that common;y used. Based on the API rather
  * than ledger for now.
  */
case class AccountData(
                        account: AccountAddress,
                        accountTxnId: Option[AccountTxnId],
                        balance: CurrencyAmount.Drops,
                        flags: Flags, // Bitmap flag, can be zero
                        ledgerEntryType: String, // Should always be AccountRoot
                        messageKey: Option[String], // 33 byte public key, but keep as Hex String for now
                        ownerCount: Long, // UInt32
                        previousTxnID: Option[TxnHash], // Not there for CreatedNode
                        previousTxnLgrSeq: Option[LedgerIndex],
                        regularKey: Option[String], // AccountId in Base58 rXXXX format! for regular keys
                        sequence: AccountTxnNumber, // Next Txn Number for Account
                        index: TxnHash, // 64 char.
                        signer_list: Option[List[Json]], // Field can not be there, or be empty array
) derives ConfiguredCodec

object AccountData {
  given Configuration = CirceCodecUtils.customConfiguration(capitalize, skipping = List("index", "signer_list"))
}
