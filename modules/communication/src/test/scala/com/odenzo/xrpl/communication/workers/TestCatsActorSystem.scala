//package com.odenzo.xrpl.communication.workers
//
//import cats.effect.{ ExitCode, IO, IOApp, Resource }
//import com.odenzo.xrpl.communication.jsonrpc.engine.{ RPCEngine,  }
//import com.suprnation.actor.{ ActorRef, ActorSystem }
//import com.tersesystems.blindsight.LoggerFactory
//import org.http4s.Uri
//import org.http4s.implicits.uri
//
//import scala.concurrent.duration.DurationInt
//
//object TestCatsActorSystem {
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
//      val wsEndpoint = uri"ws://localhost:8001/"
//      val resource   = XrplStateProxyActor.make(wsEndpoint)
//      for {
//        actorRef <- system.actorOf(XrplStateProxyActor(true, resource), "XrplStateProxyActor")
//        _         = log.info(s"Created XrplStateProxyActor $actorRef")
//        _        <- keepBusy
//      } yield ()
//    }
//  }
//
//  def keepBusy =
//    .createdAsResource(uri"http://localhost:7001/", uri"http://localhost:7001/").use { rpc =>
//      val action = IO.sleep(500.millis) *>
//        IO.blocking(log.info("Doing a Cycle")) *>
//        rpc.ledgerAccept
//
//      action.foreverM
//    }
//
//}
