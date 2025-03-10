package com.odenzo.xrpl.models.data.models.monetary

//package com.odenzo.xrpl.models.atoms
//
//import cats.*
//import cats.data.*
//import com.odenzo.xrpl.models.wireprotocol.CodecTesting
//import com.typesafe.scalalogging.StrictLogging
//import io.circe.*
//import io.circe.syntax.*
//import io.circe.Decoder.Result
//import org.scalatest.FunSuite
//
//class Ledger$Test extends FunSuite with CodecTesting with StrictLogging {
//
//  val index = LedgerIndex(666L)
//  val name  = LedgerName("lname")
//  val hash  = LedgerHash(RippleHash("SomeLongHash"))
//
//  private def multLongJson = parse("""{ "ledger_index": 777, "ledger_hash": "hashashashashash"}    """)
//  private def multNameJson = parse("""{ "ledger_index": "validated", "ledger_hash": "hashashashashash"}    """)
//  private def longJson     = parse("""{ "ledger_index": 777   } """)
//  private def nameJson     = parse("""{ "ledger_index": "validated" }   """)
//  private def hashJson     = parse("""{ "ledger_hash": "hashashashashash"}    """)
//
//  test("Encode LedgerId ") {
//    val t: LedgerId = index
//
//    testEncoding(index: LedgerId, Encoder[LedgerId]) shouldEqual Json.fromLong(index.v)
//    testEncoding(name: LedgerId, Encoder[LedgerId]) shouldEqual Json.fromString(name.v)
//
//    testEncoding(name: LedgerId, Encoder[LedgerId]) shouldEqual (name: LedgerId).asJson
//    testEncoding(t, Encoder[LedgerId]) shouldEqual t.asJson
//    testEncoding(index, Encoder[LedgerIndex]) shouldEqual index.asJson
//
//  }
//
//  /** Behaviour of the decoders for inspection. */
//  test("Exploratory Decoding") {
//    decodingScenarioa(multLongJson)
//    decodingScenarioa(multNameJson)
//    decodingScenarioa(longJson)
//    decodingScenarioa(nameJson)
//    decodingScenarioa(hashJson)
//  }
//
//  private def decodingScenarioa(json: Json) = {
//
//    val deepIndex: Result[LedgerIndex] = json.hcursor.get[LedgerIndex]("ledger_index")
//    val deepName: Result[LedgerName]   = json.hcursor.get[LedgerName]("ledger_index")
//    val shallow: Result[LedgerId]      = json.hcursor.get[LedgerId]("ledger_index")
//    val deepHash: Result[LedgerHash]   = json.hcursor.get[LedgerHash]("ledger_hash")
//    def full                           = Decoder[Ledger].decodeJson(json)
//
//    logger.debug(s"Decoding Scenarios on: ${json.spaces2}")
//    logResult(deepIndex, "Deep Index")
//    logResult(deepName, "Deep Name")
//    logResult(shallow, "Shallow LedgerId")
//    logResult(deepHash, "Deep Hash")
//    logResult(full, "Full Ledger")
//  }
//}
