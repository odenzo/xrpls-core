package com.odenzo.xrpl.models.data.models.paths

import io.circe.Codec

case class XRPLPath(steps: List[XRPLPathStep]) derives Codec.AsObject {
  def append(step: XRPLPathStep): XRPLPath = XRPLPath(this.steps.appended(step))
}

object XRPLPath {
  val empty: XRPLPath = XRPLPath(List.empty[XRPLPathStep])

}
