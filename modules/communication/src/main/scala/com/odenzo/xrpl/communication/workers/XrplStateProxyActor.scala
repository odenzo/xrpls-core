//package com.odenzo.xrpl.communication.workers
//
//import cats.effect.IO
//import com.odenzo.xrpl.communication.XrplEngineConfig
//import com.odenzo.xrpl.communication.websocket.subscription.{ SubscribeEngine, SubscribeRq }
//import fs2.{ Pipe, Stream, concurrent }
//import io.circe.{ Json, JsonObject }
//import org.http4s.Uri
//import cats.effect.*
//import cats.effect.syntax.all.*
//import cats.*
//import cats.data.*
//import cats.syntax.all.*
//import com.odenzo.xrpl.models.apis.models.ledgerids.LedgerHandle.LedgerIndex
//import com.odenzo.xrpl.models.apis.models.monetary
//import com.odenzo.xrpl.models.apis.models.monetary.CurrencyAmount
//import com.suprnation.actor.Actor.ReplyingReceive
//import com.suprnation.actor.ActorRef.ActorRef
//import com.suprnation.actor.SupervisorStrategy.{ Escalate, Restart, Stop }
//import com.suprnation.actor.{ Actor, ActorContext, ActorSystem, OneForOneStrategy, ReplyingActor, SupervisionStrategy }
//import com.tersesystems.blindsight.LoggerFactory
//
//import java.util.UUID
//import scala.annotation.unused
//import scala.concurrent.duration.given
//import scala.concurrent.duration.*
//
///**
//  * Prototyping. Want to get a Ledger Subscription and keep (mutable) state for
//  * Fees and Current Ledger etc.
//  *   - Want to enable triggering on Consensus happening
//  *   - May also want to enclude triggering on validation of a Transaction
//  *     (monitored on a per account bases). Transaction may be best on something
//  *     that checks all transactions or just per account. Or just per
//  *     transaction doing a simple get based on the consensus happening Will
//  *     this be a singleton that people "register" which to get updates, or do
//  *     we require a list of functions to happen dynamically. Lets see how it
//  *     goes. I don't like a pure object, but maybe creating a structural
//  *     object?
//  *
//  * Perhaps we use a supervisor to control this in Application app.Main type style.
//  * https://fs2.io/#/concurrency-primitives?id=single-publisher-multiple-subscriber
//  */
//import com.suprnation.actor.Actor.{ Actor, Receive }
//
//case class XrplStateMessage(json: Json)
//
//enum XrplStateProxyRequest:
//  case register(listener: ActorRef[IO, XrplStateMessage])
//  case unregister(listener: ActorRef[IO, XrplStateMessage])
//  case addCallback(fn: XrplStateMessage => IO[Boolean])
//  case removeCallback(fn: XrplStateMessage => IO[Boolean])
//  case XrplStateUpdate(m: XrplStateMessage)
//
//enum XrplStateProxyReply:
//  case XrplConsensusState(listener: ActorRef[IO, XrplStateMessage])
//  case Ack
//
///**
//  * Our actor will start polling the Ledger subscription and sending out
//  * messages on the "global" ActorSystem event bus.
//  *
//  * Alternative is to allow registration of IO callbacks (fire and forget on
//  * another Fibre) Or register another actor to forward a consensus occured
//  * state event to.
//  *
//  * Registrations are done to his Actor, which has a supervisor that can get the
//  * actorRef of it.
//  *
//  * WIP: First cut is just to log stuff out, then add the hooks to send to event
//  * bus and register listeners
//  */
//case class XrplStateProxyActor(sendToEventBus: Boolean = true) extends Actor[IO, XrplStateProxyRequest] {
//
//  private val log = LoggerFactory.getLogger
//
//  log.info(s"Running - in class initilization: ")
//
//  override def init: IO[Unit] = {
//    IO(log.info("Init Called"))
//
//  }
//  override def receive = {
//    case anything =>
//      log.info(s"Received $anything")
//    //  sender
////    case XrplStateProxyRequest.register(listener: ActorRef[IO, XrplStateMessage]) =>
////      log.info("Regsitered....")
////      IO.unit
////    case ControlMessages.stop  =>
////      log.info(s"Stopping....Closing WebSocket Subsciption")
////      IO.unit
//  }
//}
//
//object XrplStateProxyActor {
//  private val log = LoggerFactory.getLogger
//
//  def initialRequest: List[SubscribeRq] = List(SubscribeRq.validatedLedgers.withID(UUID.randomUUID().toString))
//
//  def makePipe(actorRef: ActorRef[IO, XrplStateProxyRequest], in: Stream[IO, JsonObject]): Stream[IO, JsonObject] = {
//    in.debug(j => s"Inbound: ${j.toJson.spaces2}", println(_)).map(jObj => extractLedgerState(jObj))
//  }
//
//  def makeStreamResource(
//      wsUri: Uri,
//      killSwitch: fs2.concurrent.Signal[IO, Boolean],
//  ): Resource[IO, Stream[IO, JsonObject]] = {
//    SubscribeKernel
//      .make(wsUri: Uri, initialRequest).map(
//        _.receiveStreamStd.evalMap(jo => extractLedgerState(jo)).interruptWhen(killSwitch)
//      )
//  }
//
//  def extractLedgerState(jObj: JsonObject) = {
//    IO.pure(jObj)
//  }
//
//  def construct(wsServer: Uri)(using actorSystem: ActorSystem[IO]) = {
//    for {
//      actor: Any <- actorSystem.replyingActorOf(XrplStateProxyActor(true), "LedgerTicket")
//    } yield ()
//  }
//}
//
//enum SupervisorRequests:
//  case Noop
//  case Pause
//  case Resume
//
///** Anybody ever going to send message to this? */
//case class XrplStateSupervisorActor(ref: Ref[IO, List[ActorRef[IO, SupervisorRequests]]])
//    extends Actor[IO, SupervisorRequests] {
////  override def supervisorStrategy: SupervisionStrategy[IO] =
////    OneForOneStrategy[IO](maxNrOfRetries = 3, withinTimeRange = 1.minute) {
////      case _: IllegalArgumentException => Stop
////      case _: RuntimeException         => Restart
////      case _: Exception                => Escalate
////    }
////
////  override def preStart: IO[Unit] =
////    for {
////      childActor <- context.actorOf(ChildActor(), "child-actor")
////      _          <- ref.update(_ :+ childActor)
////    } yield ()
////
////  override def receive: Receive[IO, Request] = {
////    case msg =>
////      for {
////        children <- ref.get
////        _        <- children.traverse_(child => child ! msg)
////      } yield ()
////  }
//}
//
//object XrplStateSupervisorActor {
//
//  def decodeSubmitResponseOrStreamMsg(json: Json) = {
//    // decode the stream message, fallback to decoding the submit rs
//    // Convert to whatever the status object is.
//  }
//
//  // .map {
//  //    (engine: SubscribeEngine) =>
//  //  val theBackgrounIO: IO[(OutcomeIO[Unit], OutcomeIO[Unit])] = engine.run
//  //  theBackgrounIO.background
//
//}
////
////class XrplStateProxy(nonAdminWebsocketUri: Uri) {
////  private val log = LoggerFactory.getLogger
////
////  // Make the Ledger Request, I think thats all for now.
////  val subs                                     = List.empty[SubscribeRq]
////  val pipe                                     = Option.empty[Pipe[IO, Json, Unit]]
////  val ledgerSub: Resource[IO, SubscribeEngine] = SubscribeEngine.build(nonAdminWebsocketUri, subs, pipe)
////
////  val pipeImp: Pipe[IO, Json, Unit] = streamImpl
////
////  def streamImpl(inStream: fs2.Stream[IO, Json]): fs2.Stream[IO, Unit] = {
////    inStream
////      .debug(_.spaces4, s => log.info(s"XrplStateProxy started with $s"))
////      // We will get the initial response of any subscribe
////      // then a bunch of payloads.
////      // Need to check tests to see exactly when and what info we get
////      .void
////  }
////  // class SubscriptionState() {
////  //  val subscribed = scala.collection.mutable.Map.empty[String, SubscribeRq]
////  // }
////}
////
////case class XrplState(fee: monetary.CurrencyAmount.Drops, currentLedger: LedgerIndex)
////
/////**
////  * Think may as well just decode this. Also pass in optional functions to
////  * calculate lastLedgerIndex and perhaps the given fee. I am not sure there is
////  * a FeeMax, and we (I guess) need to add to the fee for creating ledger
////  * objects. reserve_inc is the amount for each additional ledger thhing
////  * created? Maybe some smarter functions. Or forget the functions and let then
////  * just get the ref of the whole thing?
////  */
/////* Initial:
//// * "result" : { "fee_base" : 10, "fee_ref" : 10, "ledger_hash" :
//// * "D691268CCA627E3743A641DA1FC9B3AD987BEE52BF2AB433FCCC82834DFE351E", "ledger_index" : 138, "ledger_time" : 792150175,
//// * "reserve_base" : 10000000, "reserve_inc" : 2000000, "validated_ledgers" : "2-138" }, "status" : "success", "type" :
//// * "response" Then N:
//// * { "fee_base" : 10, "fee_ref" : 10, "ledger_hash" :
//// * "89E3F9B51085AB33AED6C97E77408B4274A7987F06B488B1F471932E3FB10953", "ledger_index" : 139, "ledger_time" : 792153170,
//// * "reserve_base" : 10000000, "reserve_inc" : 2000000, "txn_count" : 0, "type" : "ledgerClosed", "validated_ledgers" :
//// * "2-139" } */
