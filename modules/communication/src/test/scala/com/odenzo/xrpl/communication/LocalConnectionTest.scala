package com.odenzo.xrpl.communication

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*
import com.odenzo.xrpl.communication.config.XrplEngineConfig
import com.odenzo.xrpl.communication.rpc.RPCEngine
import com.odenzo.xrpl.communication.websocket.WSEngine
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.atoms.AccountAddress
import com.tersesystems.blindsight.LoggerFactory
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.implicits.uri

import scala.concurrent.duration.given

class LocalConnectionTest extends munit.CatsEffectSuite {

  private val log = LoggerFactory.getLogger

  // Send a PING command across each port in the local docket as per the config
  val cmd: AccountInfo.Rq = AccountInfo.Rq(AccountAddress.GENESIS)
  test("RPC Connect") {
    val target: Uri = XrplEngineConfig.localDocker.rpc
    log.info(s"Testing Target URL: ${target}")
    RPCEngine.createAsResource(target).use { engine =>
      log.info(s"About to send to $target")
      engine.send[AccountInfo.Rq, AccountInfo.Rs](cmd).map { rs =>
        log.info(s"RPC Response: ${rs}")
      }
    }
  }
  test("RPC Admin Connect") {
    val target: Uri = XrplEngineConfig.localDocker.rpcAdmin.get
    log.info(s"Testing Target URL: ${target}")
    RPCEngine.createAsResource(target).use { engine =>
      log.info(s"About to send to $target")
      engine.send[AccountInfo.Rq, AccountInfo.Rs](cmd).map { rs =>
        log.info(s"RPC Response: ${rs}")
      }
    }
  }
  test("WS Connect") {
    val target: Uri = XrplEngineConfig.localDocker.websocket // uri"ws://localhost:8080/ws"
    log.info(s"Testing Target URL: ${target}")
    WSEngine.createAsResource(target).use { engine =>
      log.info(s"About to send to $target")
      // IO.cede *> IO.sleep(2.seconds) *>

      engine.send[AccountInfo.Rq, AccountInfo.Rs](cmd).map { rs =>
        log.info(s"WS Response: ${rs}")
      }
    }
  }

  test("WS Admin Connect") {
    val target: Uri = XrplEngineConfig.localDocker.websocketAdmin.get
    log.info(s"Testing Target URL: ${target}")
    WSEngine.createAsResource(target).use { engine =>
      log.info(s"About to send to $target")
      engine.send[AccountInfo.Rq, AccountInfo.Rs](cmd).map { rs =>
        log.info(s"RPC Response: ${rs}")
      }
    }
  }

}
