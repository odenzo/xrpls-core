package com.odenzo.xrpl.models.data.models.atoms

import com.odenzo.xrpl.common.binary.{ FixedSizeBinary, XrpBinaryOps }
import com.odenzo.xrpl.common.utils.CirceCodecUtils
import com.odenzo.xrpl.models.data.models.keys.XrpPublicKey
import io.circe.Codec
import scodec.bits.{ BitVector, ByteVector }

/**
  * AccountId is the payload of AccountAddress, without the prefix or checkum.
  * https://xrpl.org/docs/references/protocol/transactions/common-fields
  */
opaque type AccountId = BitVector

object AccountId:
  given FixedSizeBinary[AccountId](20 * 8) with {
    def fromBits(bits: BitVector): AccountId = bits
    def toBits(a: AccountId): BitVector      = a
  }

  /** To and from JSON its always going to be an AccountAddress I think. */
  given Codec[AccountId]                                   = CirceCodecUtils.hexBitsCodec
  def fromBytes(b: ByteVector): AccountId                  = b.bits
  def fromBits(b: BitVector): AccountId                    = b
  def fromMasterPublicKey(master: XrpPublicKey): AccountId = {
    val ripe: ByteVector = XrpBinaryOps.ripemd160(master.bv)
    AccountId.fromBits(ripe.bits)
  }

  def fromAccountAddress(a: AccountAddress): AccountId = a.asAccountId
