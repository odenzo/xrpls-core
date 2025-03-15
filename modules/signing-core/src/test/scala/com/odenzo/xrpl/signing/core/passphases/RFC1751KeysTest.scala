package com.odenzo.xrpl.signing.core.passphases

import cats.effect.IO
import com.odenzo.xrpl.models.api.commands.*
import com.odenzo.xrpl.models.data.models.keys.{ WalletProposeResult, XrpSeed }
import com.odenzo.xrpl.signing.testkit.CommandRqRsTestDataIOSpec
import com.tersesystems.blindsight.LoggerFactory
import scodec.bits.Bases.Alphabets
import scodec.bits.ByteVector

/**
  * This is really just a test of RFC1751 works converting to a seed, compared
  * by hex. RFC functions just throw Exceptions now, most not wrapped even,
  * These are really SeedOps tests, not detailed unit tests into RFC1751 yet
  */
class RFC1751KeysTest extends CommandRqRsTestDataIOSpec[WalletPropose.Rq, WalletPropose.Rs]("WalletProposeRqRs.json") {
  import com.odenzo.xrpl.models.data.models.atoms.AccountAddress.given
  private val log                                                           = LoggerFactory.getLogger
  import com.odenzo.xrpl.models.data.models.keys.XrpSeed.given // Extension method
  def checkRFC(walletRs: WalletPropose.Rs)(using loc: munit.Location): Unit = {
    test(s"${walletRs.accountId.asBits.toHex}") {
      val seedFromResponse: XrpSeed = PassphraseOps.seedFromString(walletRs.masterKey)
      val masterSeedHex: String     = walletRs.masterSeedHex
      val hex: String               = seedFromResponse.asMasterSeedHex
      assertEquals(hex, masterSeedHex, "Master Seed Hex from RFC Incorrect")

    }
  }

  testDataResource
    .use { wallets =>
      wallets.foreach { rs => checkRFC(rs._2) }
      IO.unit
    }.unsafeRunSync()
}
