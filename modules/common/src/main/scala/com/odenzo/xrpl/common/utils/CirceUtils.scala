package com.odenzo.xrpl.common.utils

import cats.*
import cats.syntax.all.*
import com.tersesystems.blindsight.LoggerFactory
import io.circe
import io.circe.*
import io.circe.syntax.*

import scala.io.Source
import scala.util.Try

trait CirceUtils extends BlindsightLogging {
  private val log = LoggerFactory.getLogger(getClass)

  def loadResourceAsString(resourcePath: String): Either[RuntimeException, String] = {
    Try {
      val source = Source.fromResource(resourcePath)
      val text   = source.mkString
      source.close()
      text

    }.toEither.left.map(t => new RuntimeException(s"Error loading resource $resourcePath: ${t.getMessage}", t))
  }

  // Parsing Files
  // Parsing Resources
  def parseResource(resourcePath: String): Either[Exception, Json] = {
    loadResourceAsString(resourcePath).flatMap { src =>
      log.info(s"Got the Src from $resourcePath => $src")
      val t = src.mkString
      parseJson(t)
    }
  }

  inline def parseJson(src: String): Either[ParsingFailure, Json] = io.circe.parser.parse(src)

  inline def decode[T: Decoder](src: String): Either[circe.Error, T] = io.circe.parser.decode(src)

  inline def decodeJson[T: Decoder](src: Json): Either[circe.Error, T] = src.as[T]

  def decodeUnsafe[T](src: Json)(using Decoder[T]): T = {
    src.as[T] match
      case Left(err)    =>
        log.warn(s"Error Decoding Json: ${err.getMessage} for \n ${src.spaces2}")
        throw err
      case Right(value) => value

  }

  /** Ripled doesn't like objects like { x=null } */
  val droppingNullsPrinter: Printer = Printer.spaces2.copy(dropNullValues = true)

  /**
    * Converts json to formatted text dropping null JsonObject fields.
    *
    * @param json
    * @return
    */
  def print(json: Json): String = json.deepDropNullValues.noSpaces

  def printObj(jsonObject: JsonObject): String = print(jsonObject.asJson)

  /**
    * Monoid/Semigroup for Circe Json Object, so we can add them together. Not
    * safe on collisions of field names
    */
  given jsonObjectMonoid: Monoid[JsonObject] = new Monoid[JsonObject] {
    def empty: JsonObject                                 = JsonObject.empty
    def combine(x: JsonObject, y: JsonObject): JsonObject = JsonObject.fromIterable(x.toVector |+| y.toVector)
  }

}
object CirceUtils extends CirceUtils
