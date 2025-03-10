package com.odenzo.xrpl.models.data.models.paths

import com.odenzo.xrpl.models.data.models.monetary.{ XrplCurrency, XrplStdCurrency }
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import scodec.*
import scodec.bits.*
import scodec.codecs.*

case class XRPLPath(steps: List[XRPLPathStep]) derives Codec.AsObject {
  def append(step: XRPLPathStep): XRPLPath = XRPLPath(this.steps.appended(step))
}

object XRPLPath {
  val empty: XRPLPath = XRPLPath(List.empty[XRPLPathStep])

}
