package com.odenzo.xrpl.models.api.transactions

import io.circe.derivation.{ Configuration, ConfiguredEnumCodec }

/** Out of Date for SUre! */
enum XrpTxnType derives ConfiguredEnumCodec:
  case Payment
  case OfferCreate
  case OfferCancel
  case TrustSet
  case AccountSet
  case SetRegularKey
  case EscrowCreate
  case EscrowCancel
  case EscrowFinish
  case PaymentChannelCreate
  case PaymentChannelFund
  case PaymentChannelClaim
  case SignerListSet

object XrpTxnType:
  given Configuration = Configuration.default
