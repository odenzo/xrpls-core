package com.odenzo.xrpl.common.utils

import com.fasterxml.jackson.databind.JsonNode
import com.tersesystems.blindsight.*
import io.circe.*
import io.circe.syntax.*
import scodec.bits.Bases.Alphabets.HexUppercase
import scodec.bits.{ BitVector, ByteVector }
import com.tersesystems.blindsight.AST.*
import java.time.Instant

trait BlindsightUtils {
//
//  import com.tersesystems.blindsight.AST.{ *, given }
//  import com.tersesystems.blindsight.DSL.{ *, given }
  import io.circe.Encoder

  protected given ToArgument[List[String]] = { v =>
    Argument(bobj("details" -> BArray(v.map(new BString(_)))))
  }

  /**
    * This allows any T that has a Circe Encoder to be converted to String.
    * There is no real use case for this in structured logging
    */

  extension [T: Encoder](x: T)
    def asBValue: BString = {
      BString(x.asJson.noSpacesSortKeys)
    }

  extension (bv: BitVector) def asBValue: BValue  = BString(bv.toHex(HexUppercase))
  extension (bv: ByteVector) def asBValue: BValue = BString(bv.toHex(HexUppercase))

  /**
    * TODO: Place this more ergonomically into a ToArgument. This is also slow
    * as..
    */
  protected def jlog(circeJson: Json): JsonNode = {
    import com.fasterxml.jackson.databind.*
    new ObjectMapper().readTree(circeJson.spaces2)
  }

  protected given ToArgument[(String, Json)] = (tj: (String, Json)) => {
    val bval = BString(tj._2.spaces2)
    Argument(bobj(BField(tj._1, bval)))
  }

  // Make this a Valid AST Node I guess
  //  given ToArgument[circe:Json] = { circe =>
  //    circeToJacksonJsonNode(circe)
  //  }

  protected given ToArgument[(String, BValue)] = {
    case (k, v) =>
      Argument(bobj(k -> v))
  }

  /** Instant to String */
  protected def instantToBString(i: Instant): BString = new BString(i.toString)

  /** Instant to epoch millies */
  protected def instantToBTimestamp(i: Instant): BValue = new BLong(i.toEpochMilli)

  protected def jarg(key: String, json: Json): Argument = {
    val jnode = jlog(json)
    new Argument(jnode)
  }

  protected def jsonToAST(json: Json): AST.BValue = {
    json.fold(
      jsonNull    = AST.BNull,
      jsonBoolean = (v: Boolean) => BBool(v),
      jsonNumber  = (v: JsonNumber) => BString(json.noSpaces),
      jsonString  = (x: String) => BString(x),
      jsonArray   = arr => BArray(arr.map(jsonToAST).toList),
      jsonObject  = (obj: JsonObject) => {
        val ast: List[(String, BValue)] =
          obj.toList.sortBy(_._1).map { case (field, v) => BField(field, jsonToAST(v)) }
        BObject(ast)
      },
    )
  }

  protected def logArg[T: Encoder](label: String, t: T): Argument = jsonToArgument(label, t.asJson)

  protected def jsonToArgument(label: String, json: Json): Argument = {
    Argument(bobj(label -> jsonToAST(json)))
  }
  //  given jsonToArgument[T: Encoder]: ToArgument[(String, T)] = ToArgument {
  //    import com.fasterxml.jackson.databind.JsonNode
  //    case (k, instance) =>
  //      val node = JsonNode
  //      Argument(StructuredArguments.keyValue(k, node)) // or raw(k, node.toPrettyString)
  //  }

  // Some Marker helprs

  protected object markers {

    private val tlog = LoggerFactory.getLogger

    /**
      * Safe to use as log.withMarker(processMarker("foo"))... will add to
      * Markers
      */
    def processMarker(processName: String): BObject = bobj("process" -> BString(processName))

    def correlationId(id: String): BObject = bobj("traceId" -> BString(id))

    val xx: Markers   = Markers(processMarker("foo"))
    val m1log: Logger = tlog.withMarker(processMarker("foo")).withMarker(correlationId("1"))
    // m1log.markers.size
    /* import net.logstash.logback.marker.{Markers => LogstashMarkers} val loggerWithTwoMarkers =
     * entryLogger.withMarker(LogstashMarkers.append("user", "will")) */
    // def addMarker(markers: Markers, marker:Ma) =
  }
}
