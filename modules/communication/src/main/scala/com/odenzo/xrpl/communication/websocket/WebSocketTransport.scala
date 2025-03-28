package com.odenzo.xrpl.communication.websocket

import _root_.fs2.{ Stream, concurrent }
import cats.*
import cats.effect.*
import cats.effect.std.Supervisor
import cats.syntax.all.*
import com.odenzo.xrpl.communication.websocket.WebSocketTransport.extractUUIDFromIdField
import com.tersesystems.blindsight.LoggerFactory
import fs2.concurrent.Topic
import fs2.data.json.*
import fs2.data.json.circe.*
import io.circe.syntax.*
import io.circe.{ Json, JsonObject }
import org.http4s.Uri
import org.http4s.client.websocket.*
import org.http4s.jdkhttpclient.JdkWSClient

import java.util.UUID

/**
  * Connects and starts sending all messages in rq and begins receiving messages
  * and piping them through your rs Pipe. These are two concurrent IO tasks that
  * are cancelable. They need to be `run` since they are suspended in IO.
  */
class WebSocketTransport(
    val ws: WSConnectionHighLevel[IO],
    topic: Topic[IO, JsonObject],
    correlator: MessageCorrelator[UUID],
) {
  private val log = LoggerFactory.getLogger

  /**
    * This consumes messages from the internal topic and then registers with the
    * correlator and sends over the websocket to XRPL server.
    */
  val sender: Stream[IO, Unit] = topic.subscribeUnbounded.evalMap { jo =>
    val sendProg: IO[Unit] = for {
      id   <- extractUUIDFromIdField(jo)
      frame = WSFrame.Text(jo.asJson.deepDropNullValues.spaces4)
      sent <- ws.send(frame)
      _    <- correlator.sentMessageDefferedResponse(id) // This blocks
    } yield ()
    sendProg.uncancelable
  }

  /**
    * This must have an id field that is decodable as a UUID. I don't let you
    * send one in seperately which is faster, to ensure no mistakes. This is
    * essentially a helper that reterns
    */
  def send(msg: JsonObject): IO[JsonObject] = {
    val packet = msg.asJson.deepDropNullValues.spaces4
    for {
      id       <- extractUUIDFromIdField(msg)
      _        <- IO(log.info(s"Sending Outbound Message: $packet "))
      frame     = WSFrame.Text(packet, true)
      _        <- ws.send(frame)
      response <- correlator.sentMessageDefferedResponse(id) // This blocks
      _        <- IO(log.info(s"WS Transport Returned from Correlator Blocking"))
    } yield response
  }
}

object WebSocketTransport {

  private val log = LoggerFactory.getLogger

  private[websocket] def extractUUIDFromIdField(jo: JsonObject): IO[UUID] = {
    jo("id")
      .toRight(s"No 'id' found in ${jo.toJson.spaces4}")
      .flatMap(json => json.as[UUID].leftMap(err => s"'id' field found but not UUID: $err (for ${json.noSpaces})"))
      .fold(err => IO.raiseError(IllegalArgumentException(err)), IO.pure)
  }

  /**
    * This is the WebSocket receiver. It parses the JSON and calls the
    * correlator to find the sender of the message (by ID) and complete its
    * Deferred. This should run as long as this class runs.
    */
  def receiver(
      wsConnection: WSConnectionHighLevel[IO],
      correlator: MessageCorrelator[UUID],
  ): IO[Unit] = {
    IO(log.info(s"Building Receiver for WebSocket using Connection $wsConnection")) *>
      IO(log.info(s"Running the IO for reciever stream: SubProtocal ${wsConnection.subprotocol}")) *>
      wsConnection
        .receiveStream
        .map {
          case x @ WSFrame.Text(data, last) => x
          case WSFrame.Binary(data, last)   =>
            log.warn(s"Binary DataFrame Received: [${data}] Last=$last")
            WSFrame.Text("{}", true)
        }
        .collect { case WSFrame.Text(text, _) if !text.startsWith("Request") => text }
        .through(ast.parse)
        .map((json: Json) => json.asObject.getOrElse(throw IllegalArgumentException("Event not a JSON Object")))
        .debug(_.toJson.spaces4, s => log.info(s"JSON Received: $s"))
        .evalTap(jo => extractUUIDFromIdField(jo).flatMap(id => correlator.receivedMessage(id, jo)))
        .redeem(err => {
                  log.error(s"Got Error in Receive Stream: $err")
                  JsonObject.empty
                },
                identity,
               )
        .void
        .compile
        .drain
        .onCancel(IO(log.warn("Receive Stream Cancelled")))

  }

  /**
    * Builds a WebSocket Transport layer with a background thread to monitor
    * inbound websocket. The FibreIO should stop running when the resource is
    * release.
    */
  def build(serverWebsocketUri: Uri): Resource[IO, WebSocketTransport] = {
    val request: WSRequest                  = WSRequest(serverWebsocketUri)
    val correlator: MessageCorrelator[UUID] = new MessageCorrelator[UUID]

    val engine: Resource[IO, WebSocketTransport] = for {
      supervisor <- Supervisor[IO]
      wsclient   <- JdkWSClient.simple[IO]
      connection <- wsclient.connectHighLevel(request)
      topic      <- Topic[IO, JsonObject].toResource
      receiver   <-
        Resource.make(supervisor.supervise(receiver(connection, correlator)))((v: Fiber[IO, Throwable, Unit]) =>
          IO(log.info("Cacelled Receiver")) *>
            v.cancel
        )
      engine      = WebSocketTransport(connection, topic, correlator)
    } yield engine
    engine
  }
}
