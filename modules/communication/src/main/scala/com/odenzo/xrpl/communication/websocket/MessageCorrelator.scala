package com.odenzo.xrpl.communication.websocket

import cats.*
import cats.effect.*
import com.tersesystems.blindsight.LoggerFactory
import io.circe.JsonObject

import java.time.Instant
import scala.collection.concurrent.TrieMap

class MessageCorrelator[T] {

  private val log = LoggerFactory.getLogger

  /**
    * List of in-flight messages, add a timestamp to each so we can audit ones
    * not responded to. Note this TrieMap is multi-thread safe
    */
  private val pending = TrieMap.empty[T, (Deferred[IO, JsonObject], Instant)]

  /**
    * This adds the id of the message to correlation table, and returns a
    * Deferred that will be completed with the Response, to deal with unordered
    * WS request/response streams. Should be called prior to actually sending
    * the Request
    */
  def sentMessageDefferedResponse(id: T): IO[JsonObject] =
    for {
      _                <- IO.raiseWhen(pending.contains(id))(IllegalStateException(s"ID $id already registered"))
      _                 = log.info(s"Stored ID $id in Correlation Sys")
      deferredResponse <- Deferred[IO, JsonObject]()
      _                 = log.info(s"Defeferred for This Message: $deferredResponse")
      now              <- IO.realTimeInstant
      _                <- IO(pending.addOne(id, (deferredResponse, now)))
      _                <- IO(log.info(s"Message(Sender) Correlator is doing get on Deffered $deferredResponse"))
      json             <- deferredResponse.get
      _                <- IO(log.info(s"Message(Sender) Correlator Completed Getting Deferred $deferredResponse"))
    } yield json

  /**
    * We received a response, so take the ID to find the matching deferred
    * object
    */
  def receivedMessage(id: T, jo: JsonObject): IO[Boolean] =

    val findAndRelease: IO[Boolean] = pending.remove(id) match {
      case Some((deferred, sent)) =>
        IO(log.info(s"Receive Correlator Completing $id deferred $deferred")) *>
          deferred.complete(jo) <* IO(log.info(s"Receive Correlator Completed $id deferred $deferred")) <* IO.cede
      case None                   =>
        IO.raiseError(IllegalStateException(s"No deferred entry for correlation id $id"))

    }

    findAndRelease

}
