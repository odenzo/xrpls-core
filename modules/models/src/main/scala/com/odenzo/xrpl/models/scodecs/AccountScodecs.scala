package com.odenzo.xrpl.models.scodecs

import com.odenzo.xrpl.models.data.models.atoms.*
import com.tersesystems.blindsight.LoggerFactory
import scodec.*
import scodec.bits.*

/**
  * AccountId are the core of AccountAddress. We have two different codecs, one
  * for 160 bit AccountId, one for accountIdVL (variable length), And for
  * AccountAddress we have another one that encodes as FieldId :: AccountId ::
  * Checksum The FieldId is removes prior to calling the codec, but must be
  * written out. The Checksum we ignore, and just use an
  * AccountAddress.fromAccountId AcccountAddress is written out in Base58 with
  * FieldId and Checksum added. Same when an AccountAddress is encoded to
  * Binary. Its the Base58 representation in Binary. are sometimes VL Encoded,
  * and sometimealthough fixed length. These do not handle the VL Encoding or
  * consume it.
  * [[[https://developers.ripple.com/accounts.html#address-encoding]] is good
  * reference point.
  *
  * [[com.odenzo.xrpl.models.data.models.accounts.AccountAddress]] contains
  */
object AccountScodecs {
  private val log = LoggerFactory.getLogger

  // Logic: First char is 'r' <-> first byte '0'
  // Checksum is last 4 bytes, not sure how many characters all the time
  // Sometimes binary encoding includes VL sometimes not.
  // Length id slways 20 padded as needed
  // SCODEC lib is broken in this regard, always use the Base58 in com.odenzo.xrp.common

  /**
    * In non-field situations, accountId is either VLEncoded or Not, and the
    * binary is 160 always. You will have to invoke these manually I guess (so
    * far) and choose direct or accountIdVLCodec
    */
  val accountIdCodec: scodec.Codec[AccountId] = codecs
    .bits(160).xmap(
      (a: BitVector) => AccountId.fromBits(a),
      (b: AccountId) => b.asBits,
    ).withContext("AccountId")

  /**
    * Ok - Account Address as a field. Its serialized to a binary as an
    * AccountId. No Typecode no checksum. The AccountAddress itself we have to
    * match this. The JSON encoder/decoder is expecting the Base58 encoding with
    * r and checksum
    */
  val accountAddressCodec: scodec.Codec[AccountAddress] = {
    codecs
      .bits(160)
      .withContext("AccountAddress")
      .xmap(
        (a: BitVector) => {
          log.info(s"Decoding AccountAddress from AccountId")
          AccountAddress.fromAccountId(AccountId.fromBits(a))
        },
        (b: AccountAddress) => {
          log.info(s"Encoding AccountAddress field $b")
          val asAccountId = b.asAccountId.asBits
          log.info(s"As AccountId: $asAccountId")
          asAccountId
        },
      )
  }

}
