package com.odenzo.xrpl.models.data.models.atoms





//package com.odenzo.xrpl.models.accounts
//
//import com.odenzo.xrpl.common.binary.{ XrpBase58Checksum, XrpBinaryOps }
//
//import com.odenzo.xrpl.models.constants.XrpConstants
//import com.odenzo.xrpl.models.fields.{ Field, FieldVL }
//import com.odenzo.xrpl.models.fields.ids.TypePrefix
//import io.circe.Decoder.Result
//import io.circe.{ Codec, Decoder, Encoder, Json }
//import scodec.bits.{ ByteVector, hex }
//
///**
//  * AccountAddress has both a type code and field length. The ByteVector
//  * contains the whole thing. This differs from Account Address how? This is
//  * confusing between Account Address which is no VL Encoded. AccountID is in
//  * the list of Internal Type(s) -
//  * https://xrpl.org/docs/references/protocol/transactions/common-fields
//  * https://xrpl.org/docs/concepts/accounts/cryptographic-keys#account-id-and-address
//  */
//opaque type AccountXAddress = ByteVector
//
//object AccountXAddress extends Field:
//  // Prefix + accountId + (srcTag|dstTag) + XrplChecksum
//  private val totalLen                = 1 + 20 + 4 + 4 // Bytes
//  override val typePrefix: TypePrefix = TypePrefix.AccountAddress
//
//  val ACCOUNT_ZERO: Result[AccountXAddress] =
//    Codecs.base58Decoder.decodeJson(Json.fromString(XrpConstants.ACCOUNT_ZERO))
//
//  def fromAccountIdAndDestTag(accountId: AccountId, dest: DestTag): AccountXAddress =
//    checksum(typePrefix, accountId.bv)
//
//  /** FIXME */
//  def fromAccountIdAndSourceTag(accountId: AccountId, dest: SourceTag): AccountXAddress =
//    checksum(typePrefix, accountId.bv ++ dest.bv)
//
//  /** JSON Goes to XRPL Base58 Text Encoding */
//  object Codecs:
//    given base58Encoder: Encoder[AccountXAddress] = XrpBinaryOps.CirceCodecs.XrpBase58.encoderXrpBase58
//    given base58Decoder: Decoder[AccountXAddress] = XrpBinaryOps.CirceCodecs.XrpBase58.decoderXrpBase58
//end AccountXAddress
