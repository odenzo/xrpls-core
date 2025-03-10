//package com.odenzo.xrpl.models.support
//
//import io.circe.syntax._
//
//import com.typesafe.scalalogging.StrictLogging
//import org.scalatest.FunSuite
//import com.odenzo.ripple.models.wireprotocol.accountinfo.WalletProposeRq
//import com.odenzo.xrpl.models.atoms.AccountAddr
//
//class RippleCommands$Test extends FunSuite with StrictLogging {
//
//  val accAddr: AccountAddr = AccountAddr("rLqxc4bxqRVVt63kHVk22Npmq9cqHVdyR")
//  val swfRO                = RippleAccountRO("Steve Main", accAddr)
//
//
//
//  test("Basic") {
//    val jsonRq = Commands.
//      walletProposeCmd.encode(WalletProposeRq(seed = None, passphrase = Some("pewefwefhrease")))
//    logger.info(s"Request: ${jsonRq.asJson.spaces2}")
//  }
//
//}
