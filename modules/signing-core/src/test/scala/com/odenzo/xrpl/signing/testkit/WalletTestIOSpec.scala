package com.odenzo.xrpl.signing.testkit

import cats.effect.{ IO, Resource }
import com.odenzo.xrpl.models.data.models.keys.WalletProposeResult
import munit.CatsEffectSuite
import munit.catseffect.IOFixture
trait WalletTestIOSpec extends CatsEffectSuite {

  /** A list of WalletPropose responses generated from stand-alone XRPL server */
  val walletDataResource: Resource[IO, List[WalletProposeResult]] =
    TestUtils.loadListOfJsonResource[WalletProposeResult]("test/myTestData/wallets/walletRs.json")

  val walletDataFixture: IOFixture[List[WalletProposeResult]] = ResourceSuiteLocalFixture(
    "wallet-data",
    walletDataResource,
  )

  override def munitFixtures = List(walletDataFixture)

}
