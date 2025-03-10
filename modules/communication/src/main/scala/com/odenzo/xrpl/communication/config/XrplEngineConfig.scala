package com.odenzo.xrpl.communication.config

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.config.XrplEngineConfig
import org.http4s.Uri
import org.http4s.implicits.uri

case class XrplEngineConfig(
    rpc: Uri,
    rpcAdmin: Option[Uri],
    websocket: Uri,
    websocketAdmin: Option[Uri],
    debug: Boolean = true,
)

object XrplEngineConfig {

  val localDocker: XrplEngineConfig = XrplEngineConfig(
    rpc            = uri"http://127.0.0.1:8001/", // Being exposed as IPV6
    rpcAdmin       = uri"http://127.0.0.1:5006/".some,
    websocket      = uri"ws://localhost:8001/",
    websocketAdmin = uri"ws://localhost:4001/".some,
    debug          = true,
  )

  /** Mainnet from XRPL Ledger Foundation */
  val xrplLedger: XrplEngineConfig = XrplEngineConfig(
    rpc            = uri"https://xrplcluster.com/",
    rpcAdmin       = None,
    websocket      = uri"wss://xrplcluster.com/",
    websocketAdmin = None,
    debug          = true,
  )
  val testNet: XrplEngineConfig    = XrplEngineConfig(
    rpc            = uri"https://s2.ripple.com:443",
    rpcAdmin       = None,
    websocket      = uri"wss://s.altnet.rippletest.net:51233",
    websocketAdmin = None,
    debug          = true,
  )

}
