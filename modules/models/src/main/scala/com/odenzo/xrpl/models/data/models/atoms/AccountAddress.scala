package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.binary.{ FixedSizeBinary, XrpBase58Fix }
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.common.xrpconstants.{ TypePrefix, XrpConstants }
import com.odenzo.xrpl.models.data.models.fields.Field
import io.circe.{ Codec, Json }
import scodec.bits.{ BitVector, ByteVector }

/**
  * AccountAddress has both a type code and field length. The ByteVector
  * contains the whole thing. This differs from AccountId which is just 160bit
  * no prefix or checksum confusing between Account Address which is no VL
  * Encoded. AccountID is in the list of Internal Type(s) -
  * https://xrpl.org/docs/references/protocol/transactions/common-fields
  * https://xrpl.org/docs/concepts/accounts/cryptographic-keys#account-id-and-address
  *
  * This doesn't deal with the (deprecated?) account aliases, i.e. ~name Change
  * this to BinaryFixedSize? Look at now to deal with names and some new fangled
  * adressing.
  */
opaque type AccountAddress = BitVector

object AccountAddress extends Field:
  private val totalLen                = 1 + 20 + 4 // Bytes
  override val typePrefix: TypePrefix = TypePrefix.accountAddress

  given FixedSizeBinary[AccountAddress](25 * 8) with {
    def fromBits(bits: BitVector): AccountAddress = bits
    def toBits(a: AccountAddress): BitVector      = a
  }

  /** The address as type prefix and the checksum */
  val GENESIS: AccountAddress = Json.fromString(XrpConstants.GENESIS_ADDRESS).as[AccountAddress].toOption.get

  /**
    * This expects the full bytes. Basically, setUnderlying with no validation
    * at all for now
    */
  def fromRawBytes(b: ByteVector): Either[String, AccountAddress] = Right(b.bits: AccountAddress)
  def fromAccountId(accountId: AccountId): AccountAddress         = checksum(typePrefix, accountId.asBits).bits
  def fromAccountAddressB58Unsafe(s: String): AccountAddress      = XrpBase58Fix.fromValidXrpBase58(s).bits

  /**
    * In this base the r are dropped. Address must begin with r but can (I
    * think) start with rr So, this will get us N bytes of the address payload
    * plus the 4 byte checksum. We need to pad the address payload to 20 bytes
    * with 'r' and then one more 'r' Other addresses generally start with non-r
    * value as header and should not need padding. See:
    * https://xrpl.org/docs/references/protocol/data-types/base58-encodings
    */
  extension (accountAddress: AccountAddress)
    def asAccountId: AccountId = AccountId.fromBits(accountAddress.asBinary.drop(8).dropRight(32))
    def asBinary: BitVector    = accountAddress.asBits

  given Codec[AccountAddress] =
    CirceCodecUtils.xrpBase58Codec.iemap(AccountAddress.fromRawBytes)(aa => aa.asBits.bytes)
end AccountAddress
