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
  * Test data contains a mix of RFC and regular passphrases
  */
class PassphraseKeysTest
    extends CommandRqRsTestDataIOSpec[WalletPropose.Rq, WalletPropose.Rs]("WalletProposeRqRs.json") {

  private val log                                                     = LoggerFactory.getLogger
  import com.odenzo.xrpl.models.data.models.atoms.AccountAddress.given
  import com.odenzo.xrpl.models.data.models.keys.XrpSeed.given
  def checkRFC(rs: WalletPropose.Rs)(using loc: munit.Location): Unit = {
    test(s"${rs.accountId.asBits.toHex} - ${rs.keyType}") {
      val rfcPassphrase: String = rs.masterKey
      log.debug(s"MasterKey: [$rfcPassphrase]")
      val masterSeedHex: String = rs.masterSeedHex
      val seed: XrpSeed         = RFC1751Keys.twelveWordsAsBytes(rfcPassphrase)
      val hex: String           = seed.asMasterSeedHex
      assertEquals(hex, masterSeedHex)

    }
  }

  testDataResource
    .use { rqrs =>
      rqrs.foreach { rs => checkRFC(rs._2) }
      IO.unit
    }.unsafeRunSync()
}
