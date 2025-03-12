package com.odenzo.xrpl.models.data.models.constants

import cats.Eq
import scodec.bits.ByteVector

/**
  * https://xrpl.org/docs/references/protocol/data-types/base58-encodings The
  * table will all the encodings the XRP Ledger Uses
  *
  * @param byte
  */
enum TypePrefix(val prefix: Byte):
  case accountAddress extends TypePrefix(0x00)
  case accountPublicKey extends TypePrefix(0x23)
  case seedValue extends TypePrefix(0x21)
  case validationPublicKey extends TypePrefix(0x1c)

  def bv: ByteVector = ByteVector(prefix)

object TypePrefix {}
