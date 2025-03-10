package com.odenzo.xrpl.models.data.models.paths

import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.odenzo.xrpl.models.data.models.monetary.XrplCurrency
import io.circe.Codec
import scodec.bits.*

/** TxnPathSet for paths converting money in transactions. */
case class XRPLPathStep(
    account: Option[AccountAddress] = None,
    currency: Option[XrplCurrency]  = None,
    issuer: Option[AccountAddress]  = None,
) derives Codec.AsObject {

  val code: ByteVector = (account, currency, issuer) match {
    case (Some(a), None, None)    => hex"01"
    case (None, Some(c), None)    => hex"10"
    case (None, None, Some(i))    => hex"20"
    case (None, Some(c), Some(i)) => hex"30"
    case _                        => throw new IllegalStateException("Invalid Pathstep")
  }

}
