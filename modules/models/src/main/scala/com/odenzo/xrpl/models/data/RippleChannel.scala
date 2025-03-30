package com.odenzo.xrpl.models.data

//package com.odenzo.xrpl.models.atoms
//
//import CurrencyAmount.Drops
//import com.odenzo.xrpl.common.UInt32

import com.odenzo.xrpl.models.data.atoms.RippleHashes.ChannelIndex
import com.odenzo.xrpl.models.data.atoms.{ AccountAddress, DestTag, SourceTag, XrplTime }
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.keys.XrpPublicKey.given
import com.odenzo.xrpl.models.data.monetary.CurrencyAmount.Drops
import io.circe.derivation.{ Configuration, ConfiguredCodec }

object RippleChannel:
  given Configuration = Configuration.default.withSnakeCaseMemberNames

/**
  * Represents a Ripple Payment Channel, as found in AccountChannels inquiry
  * reponse Details at: https://ripple.com/build/rippled-apis/#account-channels
  */
case class RippleChannel(
                          account: AccountAddress,
                          amount: Drops,
                          balance: Drops,
                          channelId: ChannelIndex,
                          destinationAccount: AccountAddress,
                          publicKey: Option[XrpPublicKey],
                          settleDelay: Int,
                          expiration: Option[XrplTime],
                          cancelAfter: Option[XrplTime],
                          sourceTag: Option[SourceTag], // TODO: Make more speciifc., this id dt=llll
                          destinationTag: Option[DestTag],
) derives ConfiguredCodec
