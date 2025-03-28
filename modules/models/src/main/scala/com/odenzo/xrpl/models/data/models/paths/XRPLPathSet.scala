package com.odenzo.xrpl.models.data.models.paths

import io.circe.Codec

case class XRPLPathSet(paths: Vector[XRPLPath]) derives Codec.AsObject

object XRPLPathSet {
  val empty: XRPLPathSet = XRPLPathSet(Vector.empty[XRPLPath])

}
