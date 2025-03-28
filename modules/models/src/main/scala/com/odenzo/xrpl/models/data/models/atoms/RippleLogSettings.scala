package com.odenzo.xrpl.models.data.models.atoms

object RippleLogSettings {}

//package com.odenzo.xrpl.models.atoms
//
//import cats.implicits.*
//import io.circe.Decoder.{ Result, decodeString }
//import io.circe.Encoder.encodeString
//import io.circe.generic.semiauto.deriveDecoder
//import io.circe.{ Decoder, Encoder, Json, JsonObject }
//
//import scala.collection.immutable
//
///**
//  * Setting per partition, encoded are field:val in enclosing json object
//  * (levels)
//  */
//case class RippleLogSetting(part: RippleLogPartitions, level: RippleLogLevel)
//
//object RippleLogSetting {
//  given decoder: Decoder[RippleLogSetting] = deriveDecoder[RippleLogSetting]
//}
//
//case class RippleLogLevels(partitions: List[RippleLogSetting])
//
//object RippleLogLevels {
//  //
//  //
//  //  /** The actual conversion which has a  "key": "value" like Json structure
//  //
//  //    **/
//  //  def convert(logPartition: String, levelStringAsJson: Json): Either[String, RippleLogSetting] = {
//  //    // Wacked approach for now is to use Json Decoders for both the Key and the value
//  //    val keyJson: Json = Json.fromString(logPartition)
//  //    val partition: Result[RippleLogPartitions] = keyJson.as[RippleLogPartitions] // To an enumeration now.
//  //    val level: Result[RippleLogLevel] = levelStringAsJson.as[RippleLogLevel]
//  //    val screamed: Result[RippleLogSetting] = (partition, level).mapN(RippleLogSetting.apply)
//  //    screamed.leftMap(err => err.show)
//  //  }
//  //
//  //
//  //  /**
//  //    * Applied on the levels object within result:
//  //    * *
//  //    * * <pre>
//  //    * * "result" : {
//  //    * * "levels" : {
//  //    * * "Amendments" : "Info",
//  //    * * "Application" : "Info",
//  //    * * "Collector" : "Info",
//  //    * * "Consensus" : "Info",
//  //    * * "FeeVote" : "Info",
//  //    * * "Flow" : "Info",
//  //    * * ...
//  //    * * </pre>
//  //    */
//  implicit val decoder: Decoder[RippleLogLevels] =
//    Decoder.decodeJsonObject.emap[RippleLogLevels] { (levelsObj: JsonObject) =>
//      val eachEntry: List[(String, Json)] =
//        levelsObj.toList // Key and value I think. and then pass on to RippleLogLevel?
//
//      val settings: Result[List[RippleLogSetting]] = eachEntry.traverse {
//        case (key: String, value: Json) => convert(key, value)
//      }
//
//      settings.leftMap(_.toString).map((l: List[RippleLogSetting]) => RippleLogLevels(l))
//    }
//
//  def convert(key: String, value: Json): Result[RippleLogSetting] = {
//    val logLevel: Result[RippleLogLevel]   = value.as[RippleLogLevel]
//    val logKey: Result[RippleLogPartitions] = Json.fromString(key).as[RippleLogPartitions]
//    (logKey, logLevel).mapN(RippleLogSetting.apply)
//  }
//
//}
