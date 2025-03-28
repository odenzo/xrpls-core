package com.odenzo.xrpl.signing.testkit

import com.tersesystems.blindsight.LoggerFactory
import io.circe.*

import scala.annotation.unused
import scala.io.{ BufferedSource, Source }
import scala.util.chaining.given

trait TestUtils {
  @unused
  private val log = LoggerFactory.getLogger

  def loadResourceNamed(fileName: String): String = {
    val source: BufferedSource = Source.fromResource(fileName)(codec = scala.io.Codec.UTF8)
    source.getLines().mkString
  }

  def loadResourceNamedAsJson(fileName: String): Json = {
    loadResourceNamed(fileName).pipe { (str: String) =>
      io.circe.parser.parse(str) match
        case Left(err)    => throw Exception(s"Error Parsing JSON from Resource $fileName", err)
        case Right(value) => value
    }
  }

}

object TestUtils extends TestUtils
