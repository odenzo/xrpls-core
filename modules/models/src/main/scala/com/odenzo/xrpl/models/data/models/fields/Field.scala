package com.odenzo.xrpl.models.data.models.fields

import cats.data.Validated
import cats.syntax.all.given
import com.odenzo.xrpl.common.binary.{ HashOps, XrpBinaryOps }

import com.odenzo.xrpl.models.data.models.fields.ids.FieldLen
import com.odenzo.xrpl.models.xrpconstants.TypePrefix
import scodec.bits.{ BitVector, ByteVector }
// See: https://xrpl.org/docs/references/protocol/binary-format

/**
  * Denotes a model that has a one byte FieldPrefix. The field may be serialized
  * for XRPL has Base58 or Hex Do all fields have a Checksum?
  * https://xrpl.org/docs/references/protocol/binary-format#field-ids
  */
trait Field extends XrpBinaryOps:
  def typePrefix: TypePrefix
  def typePrefixIs(target: Byte): Validated[String, Byte] =
    Validated.cond(
      typePrefix.prefix === target,
      target,
      s"TypePrefix $typePrefix didn't match given $target",
    )

  def checksum(typePrefix: TypePrefix, body: BitVector): ByteVector =
    typePrefix.bv ++ body.bytes ++ HashOps.xrpChecksum(typePrefix.bv ++ body.bytes)

/**
  * A field with a Type Prefix and a Variable Len Encoding The Length is in unit
  * of bits
  */
trait FieldVL extends Field {
  def fieldLen: FieldLen
}
