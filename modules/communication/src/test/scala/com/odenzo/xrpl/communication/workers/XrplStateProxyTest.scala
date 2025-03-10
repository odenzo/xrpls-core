//package com.odenzo.xrpl.communication.workers
//
//import cats.effect.*
//import cats.effect.syntax.all.*
//
//import cats.*
//import cats.data.*
//import cats.syntax.all.*
//
//import cats.effect.{Deferred, ExitCode, IO, IOApp, Resource}
//import com.odenzo.xrpl.communication.jsonrpc.TestHelpers.localDockerConfig
//import com.odenzo.xrpl.communication.jsonrpc.engine.RPCEngine
//import com.odenzo.xrpl.communication.websocket.subscription.{SubscribeEngine, SubscribeRq}
//import com.suprnation.actor.{ActorRef, ActorSystem}
//import com.tersesystems.blindsight.LoggerFactory
//import fs2.concurrent.{SignallingRef, Topic}
//import io.circe.JsonObject
//import org.http4s.Uri
//import org.http4s.implicits.uri
//
//import scala.concurrent.duration.DurationInt
//
//class XrplStateProxyTest extends munit.CatsEffectSuite {
//
//  private val log = LoggerFactory.getLogger
//
//  test("TrialRun") {
//    XrplStateProxyTest.withXrpStateProxy(uri"ws://localhost:8001/")
//      *> IO.sleep(25.seconds)
//  }
//
//}
//
//object XrplStateProxyTest {
//
//  private val log = LoggerFactory.getLogger
//
//  val actorSystemResource: Resource[IO, ActorSystem[IO]] = ActorSystem[IO]("TestActorSystem")
//
//  /**
//    * Starts the Actor System with XrplStateProxyActor, executes testIO with the
//    * ActorRef for it, then releases the ActorSystem via the standard Resource
//    * close mechanism
//    */
//  def withXrpStateProxy(
//      websocketServer: Uri
//  ): IO[Unit] = {
//    actorSystemResource.use { (system: ActorSystem[IO]) =>
//      // If we can make a IO  F[Actor[F,Request]] in business for restarts as long as we die OK
//      //   val resource = XrplStateProxyActor.make(websocketServer)
//
//      for {
//        switch   <- SignallingRef.of[IO, Boolean](false)
//        actorRef <- system.actorOf(XrplStateProxyActor(true, resource), "XrplStateProxyActor")
//        _         = log.info(s"Created XrplStateProxyActor $actorRef")
//        _        <- keepBusy
//      } yield ()
//    }
//  }
//
//  /** This does what, suspends the creation of the Actor but immediately starts the SubcribeEngine to get LedgerClosed messages
//   *  The SubscribeEngine will run in the background, we ignore the thread.
//   *  It does as a Kill signal though.
//   *  We also want it to send a Poison Pill or creates an exception in the actor, so maybe try and combine somehow. */
//  def generateActor: IO[XrplStateProxyActor] = {
//
//    // val topicRaw: IO[Topic[IO, JsonObject]] = Topic[IO, JsonObject]
//    // val topic: fs2.Stream[IO, Topic[IO, JsonObject]] = fs2.Stream.eval(Topic[IO, JsonObject])
//    // val topicR: Resource[IO, Topic[IO, JsonObject]] = Resource.eval(Topic[IO, JsonObject])
//
//    for {
//      topic <- Topic[IO, JsonObject]
//      killSwitch <- SignallingRef.of[IO, Boolean](false)
//      engine <-   SubscribeEngine.build(localDockerConfig.websocket, List(SubscribeRq.validatedLedgers),None, topic.some).background
//      actor   =
//    } yield actor
//
//        .use { (engine: SubscribeEngine, topic: Topic[IO, JsonObject]) =>
//          val subA         = topic.subscribe(5).foreach(jo => IO(log.info(s"SubA  $jo"))).compile.drain
//          val subB         = topic.subscribeUnbounded.foreach(jo => IO(log.info(s"SubB $jo"))).delayBy(15.seconds).compile.drain
//          val tickleLedger = (sendLedgerAdvance(5) *> IO.sleep(10.seconds)).replicateA_(10)
//
//          val withEngine                                             = subA &> subB &> tickleLedger
//          val theBackgrounIO: IO[(OutcomeIO[Unit], OutcomeIO[Unit])] = .flatTap { outcomes =>
//            IO(log.warn("Outcomes: " + outcomes.toList.mkString("\n")))
//          }
//          val concurrent                                             = IO.both(theBackgrounIO.timeoutAndForget(2.minutes), withEngine)
//          IO(log.info(s"About to run in background tests")) *> concurrent *> IO(log.warn("Workers Done"))
//
//        }
//  }
//
//}
