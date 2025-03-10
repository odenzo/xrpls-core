package com.odenzo.xrpl.signing.facade




//package com.odenzo.ripple.signing
//
//import io.circe.Json
//
//import cats.*
//import cats.data.*
//
//
//
///**
//  * These API are more or less drop-in replacements for calling the rippled
//  * server for signing and multisigning. API is designed with minimal
//  * dependencies. Currently just JsonObject from Circe but thinking of adding
//  * String based requests also.
//  */
//object XrplFacade {
//
//  /**
//    * Request and Response objects documented at https://xrpl.org/sign.html
//    * @param signRq
//    *   Full Request
//    * @return
//    *   Either a success or error JSON Object per Response Format
//    */
//  def sign(signRq: Json): Json = {
//    val actioned: Either[Json, Json] = SignMsg.processSignRequest(signRq)
//    actioned.fold(identity, identity)
//  }
//
//  /**
//    * Documented at https://xrpl.org/sign_for.html (Error or Success)
//    * @return
//    *   SignForRs Note that hash and tx_blob not updated yet
//    */
//  def signFor(signForRq: Json): Json = {
//    SignForMsg.signFor(signForRq).fold(identity, identity)
//  }
//
//  /** Full message based interface per https://xrpl.org/wallet_propose.html */
//  def walletPropose(walletProposeRq: Json): Json = {
//    WalletProposeMsg.propose(walletProposeRq).fold(identity, identity)
//  }
//
//}
