package com.odenzo.xrpl.models.data.models.paths

import com.odenzo.xrpl.models.data.models.monetary.{ XrplCurrency, XrplStdCurrency }
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import scodec.*
import scodec.bits.*
import scodec.codecs.*

case class XRPLPathSet(paths: Vector[XRPLPath]) derives Codec.AsObject

object XRPLPathSet {
  val empty: XRPLPathSet = XRPLPathSet(Vector.empty[XRPLPath])

}
