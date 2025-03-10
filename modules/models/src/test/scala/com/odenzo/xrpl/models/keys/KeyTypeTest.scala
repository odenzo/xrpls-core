package com.odenzo.xrpl.models.keys

//package com.odenzo.xrpl.common.models.keys
//
//import com.odenzo.xrpl.common.utils.ELogging
//import io.circe.testing.{ ArbitraryInstances, CodecLaws, CodecTests }
//import cats.Eq
//import org.scalacheck.{ Arbitrary, Gen, Prop, Properties }
//
//import scala.math.Integral.Implicits
//import munit.ScalaCheckSuite
//import org.scalacheck.Prop.*
//
//class KeyTypeTest extends munit.FunSuite {
//
//  test("Nothing Much to Test") {
//    val from   = KeyType.ed25519
//    val zenith = KeyType.valueOf(from.toString)
//    assertEquals(zenith, from)
//  }
//}
//
//class KeyTypePropertyTest extends munit.ScalaCheckSuite with ELogging with ArbitraryInstances {
//
//  // override def laws: CodecLaws[KeyType] = unserializableCodec
//  given Eq[KeyType]        = Eq.fromUniversalEquals
//  given Arbitrary[KeyType] = Arbitrary {
//    Gen.oneOf[KeyType](KeyType.values.toSeq)
//  }
//
//  val theTests: CodecTests[KeyType] = CodecTests[KeyType]
//  val uc: theTests.RuleSet          = theTests.unserializableCodec
//  val properties: Properties        = uc.all
//  properties.check()
//}
