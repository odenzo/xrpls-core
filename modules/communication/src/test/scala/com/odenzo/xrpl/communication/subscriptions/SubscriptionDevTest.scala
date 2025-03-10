package com.odenzo.xrpl.communication.subscriptions

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.communication.commands.AdminCommandsTest
import com.odenzo.xrpl.communication.config.XrplEngineConfig
import com.odenzo.xrpl.communication.rpc.engine.RPCEngine
import com.odenzo.xrpl.communication.websocket.subscription.{SubscribeEngine, SubscribeRq}
import com.odenzo.xrpl.models.api.commands.admin.LedgerAccept
import com.odenzo.xrpl.models.data.models.keys.KeyType.secp256k1
import com.tersesystems.blindsight.LoggerFactory
import fs2.Pipe
import fs2.concurrent.Topic
import io.circe.JsonObject
import munit.Tag
import org.http4s.implicits.uri

import scala.concurrent.duration.{Duration, given}

/** Normally a Fixture would be used, but since testing it directly. */
class SubscriptionDevTest extends LocalCommsTest("rpc") {

  private val log = LoggerFactory.getLogger

  override def munitIOTimeout: Duration   = 1.minute
  val localDockerConfig: XrplEngineConfig = XrplEngineConfig.localDocker
  val tags: Set[Tag]                      = Set(Tag("RequiresXrpl"))

  def sendLedgerAdvance(n: Int): IO[Unit] =
    given engine: XrplEngine                              = engineFixture()
    val cmd: IO[XrplEngineCommandResult[LedgerAccept.Rs]] =
      engine.ledgerAccept <* IO(log.info("Sending Ledger Accept")) <* IO.sleep(5.seconds)
    cmd.replicateA(n).void

  val monitorPipe: Pipe[IO, JsonObject, JsonObject] = (in: fs2.Stream[IO, JsonObject]) =>
    in.foreach(jo => IO(log.info(s"Pipe Got a JsonObject: ${jo.toJson.spaces4}")))

  test("PipeOnly") {

    /** There is currently no latch to use to shutdown the engine. */
    SubscribeEngine.build(localDockerConfig.websocket, List(SubscribeRq.validatedLedgers), monitorPipe.some, None).use {
      (engine: SubscribeEngine) =>
        val theBackgrounIO: IO[(OutcomeIO[Unit], OutcomeIO[Unit])] = engine.run
        IO(log.info(s"About to run in background tests")) *>
          theBackgrounIO.timeoutAndForget(25.seconds).start *>
          sendLedgerAdvance(5) *>
          IO.never

    }
  }

  test("Topic and Pipe") {

    val topicRaw: IO[Topic[IO, JsonObject]]          = Topic[IO, JsonObject]
    val topic: fs2.Stream[IO, Topic[IO, JsonObject]] = fs2.Stream.eval(Topic[IO, JsonObject])
    val topicR: Resource[IO, Topic[IO, JsonObject]]  = Resource.eval(Topic[IO, JsonObject])

    topicR
      .flatMap(topic =>
        SubscribeEngine
          .build(localDockerConfig.websocket, List(SubscribeRq.validatedLedgers), monitorPipe.some, topic.some)
          .tupleRight(topic)
      )
      .use { (engine: SubscribeEngine, topic: Topic[IO, JsonObject]) =>
        val subA         = topic.subscribe(5).foreach(jo => IO(log.info(s"SubA  $jo"))).compile.drain
        val subB         = topic.subscribeUnbounded.foreach(jo => IO(log.info(s"SubB $jo"))).delayBy(15.seconds).compile.drain
        val tickleLedger = (sendLedgerAdvance(5) *> IO.sleep(10.seconds)).replicateA_(10)

        val withEngine                                             = subA &> subB &> tickleLedger
        val theBackgrounIO: IO[(OutcomeIO[Unit], OutcomeIO[Unit])] = engine.run.flatTap { outcomes =>
          IO(log.warn("Outcomes: " + outcomes.toList.mkString("\n")))
        }
        val concurrent                                             = IO.both(theBackgrounIO.timeoutAndForget(2.minutes), withEngine)
        IO(log.info(s"About to run in background tests")) *> concurrent *> IO(log.warn("Workers Done"))

      }
  }

}
