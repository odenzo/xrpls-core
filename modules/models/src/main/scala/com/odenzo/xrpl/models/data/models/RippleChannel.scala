package com.odenzo.xrpl.models.data.models

//package com.odenzo.xrpl.models.atoms
//
//import CurrencyAmount.Drops
//import com.odenzo.xrpl.common.UInt32

import com.odenzo.xrpl.models.data.models.atoms.RippleHashes.ChannelIndex
import com.odenzo.xrpl.models.data.models.atoms.{ AccountAddress, DestTag, RippleTime, SourceTag }
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey.Codecs.given
import com.odenzo.xrpl.models.data.models.monetary.CurrencyAmount.Drops
import io.circe.derivation.{ Configuration, ConfiguredCodec }

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
    expiration: Option[RippleTime],
    cancelAfter: Option[RippleTime],
    sourceTag: Option[SourceTag], // TODO: Make more speciifc., this id dt=llll
    destinationTag: Option[DestTag],
) derives ConfiguredCodec

object RippleChannel:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
