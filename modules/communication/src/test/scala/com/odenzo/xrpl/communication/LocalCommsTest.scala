package com.odenzo.xrpl.communication

import cats.effect.{ IO, Resource }
import com.odenzo.xrpl.common.utils.MyLogging
import com.odenzo.xrpl.communication.EngineFunctions.SendCmdFn
import com.odenzo.xrpl.communication.websocket.WSEngine
import com.odenzo.xrpl.communication.*
import com.odenzo.xrpl.communication.config.XrplEngineConfig
import com.odenzo.xrpl.communication.rpc.RPCEngine
import com.odenzo.xrpl.models.api.commands.CommandMarkers
import com.tersesystems.blindsight.LoggerFactory
import munit.*
import munit.catseffect.IOFixture
import org.http4s.Uri

import scala.concurrent.duration.Duration
import scala.util.Random

/** Test Framework with RPC Engine pointed to LocalHost Docker Admin port */
trait LocalCommsTest(mode: String = "rpc") extends munit.CatsEffectSuite with CatsEffectFunFixtures with MyLogging {

  private val log                       = LoggerFactory.getLogger
  val random                            = new Random()
  override def munitIOTimeout: Duration = Duration(3, "min")

  protected val engineResource: Resource[IO, XrplEngine] =
    if mode.equalsIgnoreCase("rpc")
    then
      val endpoint = XrplEngineConfig.localDocker.rpcAdmin.get
      RPCEngine.createAsResource(endpoint)
    else
      val wsUri: Uri = XrplEngineConfig.localDocker.websocketAdmin.get
      WSEngine.createAsResource(wsUri)

  val engineFixture: IOFixture[XrplEngine] = ResourceSuiteLocalFixture(s"XRPL Engine: $mode", engineResource)

  override def munitFixtures: List[IOFixture[XrplEngine]] = List(engineFixture)

}
