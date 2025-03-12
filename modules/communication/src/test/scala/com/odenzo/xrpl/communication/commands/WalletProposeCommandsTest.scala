package com.odenzo.xrpl.communication.commands

import cats.effect.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.{ LocalCommsTest, TestScenarios, XrplEngine }
import com.odenzo.xrpl.models.api.commands.admin.keygen.WalletPropose
import com.odenzo.xrpl.models.data.models.keys.KeyType
import com.tersesystems.blindsight.LoggerFactory
import io.circe.syntax.*

/**
  * Some addition WalletPropose tests, used to generate test data for
  * Self-Signing Stuff.
  */
class WalletProposeCommandsTest extends LocalCommsTest(TestScenarios.mode) {

  private val log = LoggerFactory.getLogger

  val rfcPassphrases: List[String] = List(
    "ROME MUG FRED SCAN LIVE LIME",
    "CORE BOND BARN DRIP FOND LOAN",
    "FAST BARN WIND FURT LOST WOOL",
    "FACT BOND WILD FURT LOST WOOL",
    "TILE BENT FERN LOST WOOL FURT",
    "WILD BOND FERN LOST WOOL FURT",
    "MILD BOND FERN LOST WOOL FURT",
    "WIND BOND FERN LOST WOOL FURT",
    "FERN BOND WILD LOST WOOL FURT",
    "BOND FERN WILD LOST WOOL FURT",
    "I IRE BOND BOW TRIO LAID SEAT GOAL HEN IBIS IBIS DARE",
  )

  /** Generate random alphanumeric password betweet 10 and 20 characters */
  def randomPassphrase: String  = random.alphanumeric.take(random.between(10, 20)).mkString
  val passphrases: List[String] = List.fill(15)(randomPassphrase)

  def checkPassphrase[T](
      phrase: String,
      keyType: KeyType,
  )(implicit loc: munit.Location): Unit = {
    test(s"RFC $phrase") {
      //  println(s"IN:: Phrase: $phrase $keyType")
      given engine: XrplEngine           = engineFixture()
      val rq                             = WalletPropose.Rq(seed = None, passphrase = phrase.some, keyType)
      // log.info(rq.asJson.spaces4)
      val response: IO[WalletPropose.Rs] = engine.send[WalletPropose.Rq, WalletPropose.Rs](rq).map(_.rs)
      response.flatTap(answer =>
        println(s"${answer.asJson},")
        log.info(s"\n-->Answer:\n ${answer.asJson}").pure
      )

    }
  }
  val allTypes = passphrases ++ rfcPassphrases
  for (keyType <- KeyType.values)
    allTypes.foreach(phrase => checkPassphrase(phrase, keyType))

}
