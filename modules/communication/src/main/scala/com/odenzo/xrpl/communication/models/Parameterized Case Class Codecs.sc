import io.circe.{ Codec, Decoder, Encoder, JsonObject }

case class Bar(age: Int) derives Codec.AsObject
case class Foo[T: Encoder: Decoder](bar: String, bestDrink: T)

implicit def makeEncoder[T](using enc: Encoder[T]): Encoder[Foo[T]] =
  Encoder.AsObject.instance[Foo[T]] { v =>
    JsonObject(
      "bar" := v.bar,
      "specials" := v.bestDrink,
    )
  }

val foorec: Foo[Bar] = Foo[Bar]("LastChangeInn", Bar(21))
val a                = ("true", 22).asJson
val b                = foorec.asJson
// given [T <: XrpCommandRs: Decoder: Encoder] => Encoder[XrplEngineCommandResult[T]]             = deriveEncoder

import io.circe.*
import io.circe.syntax.given

case class Bar(age: Int) derives Codec.AsObject
case class Foo[T: Encoder: Decoder](bar: String, bestDrink: T)

implicit def makeEncoder[T](using enc: Encoder[T]): Encoder.AsObject[Foo[T]] =
  Encoder.AsObject.instance[Foo[T]] { v =>
    JsonObject(
      "bar" := v.bar,
      "specials" := v.bestDrink,
    )
  }

implicit def makeEDecoder[T](using dec: Decoder[T]): Decoder[Foo[T]] =
  Decoder[Foo[T]] { v => Decoder.instance[Foo[T]] { c=>
    c.get("bar")

  }
    JsonObject(
      "bar" := v.bar,
      "specials" := v.bestDrink,
    )
  }

def test() = {
  val foorec: Foo[Bar] = Foo[Bar]("LastChangeInn", Bar(21))
  val a                = ("true", 22).asJson
  val b                = foorec.asJsonObject
  println(s"A = ${a.spaces2}")
  println(s"B= ${b.toJson.spaces2}")
  val rb = b.toJson.as[Foo[Bar]]
}
test()

