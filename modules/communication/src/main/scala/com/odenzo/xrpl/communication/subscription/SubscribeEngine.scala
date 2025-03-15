package com.odenzo.xrpl.communication.subscription

import org.http4s.client.websocket.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.config.XrplEngineConfig
import com.tersesystems.blindsight.LoggerFactory
import fs2.concurrent.Topic
import fs2.data.json.ast
import _root_.fs2.{ Fallible, Pipe, Pull, Stream, concurrent }
import io.circe.{ Json, JsonObject }
import org.http4s.Uri
import org.http4s.client.{ Client, RequestKey }
import org.http4s.dsl.impl.Responses.UpgradeRequiredOps
import org.http4s.ember.client.{ EmberClient, EmberClientBuilder, EmberConnection }
import org.http4s.netty.client.NettyWSClientBuilder
import org.typelevel.keypool.KeyPool
import io.circe.syntax.*
import org.http4s.jdkhttpclient.{ JdkHttpClient, JdkWSClient }
import fs2.data.json.*
import fs2.data.json.circe.{ *, given }

/**
  * Connects and starts sending all messages in rq and begins receiving messages
  * and piping them through your rs Pipe. These are two concurrent IO tasks that
  * are cancelable. They of course need to be `run` since they are suspended in
  * IO.
  */
class SubscribeEngine(
    ws: WSConnection[IO],
    rq: List[SubscribeRq],
    pipeOut: Option[Pipe[IO, JsonObject, JsonObject]],
    topicToPublish: Option[Topic[IO, JsonObject]],
) {
  private val log = LoggerFactory.getLogger

  log.info(s"Initializing subscription engine...$rq")

  /**
    * Build the receiver stream, optionally including sending to Pipe or
    * publishing to Topic. This receives the responses from WebSocket and
    * (typically) forwards them to Topic which anyone with the handle can
    * subscribe to.
    */
  val receiver: Stream[IO, Unit] = {
    val base: Stream[IO, JsonObject] = ws
      .receiveStream
      .collect { case WSFrame.Text(text, _) => text }
      .evalTap(text => IO.println(s"Received: $text"))
      .through(ast.parse)
      .map((json: Json) => json.asObject.getOrElse(throw IllegalArgumentException("Event not a JSON Object")))
      .debug(_.toJson.spaces4, s => log.info(s"JSON Received: $s"))

    val optWithTopic: Stream[IO, JsonObject] = topicToPublish match
      case None        => base
      case Some(topic) => base.through(topic.publish)

    val throughPipe: Stream[IO, Unit] = pipeOut match
      case Some(pipe) => optWithTopic.through(pipe).void
      case None       => optWithTopic.void

    throughPipe

  }

  inline private def toDataFrame(rq: SubscribeRq): WSFrame.Text = WSFrame.Text(rq.asJson.deepDropNullValues.spaces4)

  /** No point in making a sender FS2, this is fire and receive responses */
  private val sendInitialRequests: IO[Unit] = ws.sendMany(this.rq.map(toDataFrame))

  /**
    * With two cancellable IO (one for sender, one for receiver) this has
    * IO.bothOutcome. They should run forever unless they are cancelled, or the
    * resource goes away. We don't use stream concurrently because we only have
    * one stream
    */
  def run: IO[(OutcomeIO[Unit], OutcomeIO[Unit])] = IO.bothOutcome(sendInitialRequests, receiver.compile.drain)

}

object SubscribeEngine {

  private val log = LoggerFactory.getLogger

  def build(
      serverWebsocketUri: Uri,
      rq: List[SubscribeRq],
      pipe: Option[Pipe[IO, JsonObject, JsonObject]],
      topicToPublish: Option[Topic[IO, JsonObject]],
      killSwitch: Option[concurrent.Signal[IO, Boolean]] = None, // To Do: Add the interrupt(killSwitch)
  ): Resource[IO, SubscribeEngine] = {
    val request: WSRequest = WSRequest(serverWebsocketUri)
    log.info(s"Building SubscribeEngine with $request")
    for {
      wsclient   <- JdkWSClient.simple[IO]
      connection <- wsclient.connect(request)
      _           = log.info(s"Created wsclient: ${wsclient}")
      engine      = SubscribeEngine(connection, rq, pipe, topicToPublish)
      _           = log.info(s"Created Engine $engine")
    } yield engine
  }

}
