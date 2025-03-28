import scodec.bits.{ *, given }

import scala.compiletime.*
import scala.compiletime.ops.int.S

def foo[T](using v: ValueOf[T]): T = v.value
val x = foo[13]

transparent inline def toIntC[N]: Int =
  inline constValue[N] match
    case 0        => 0
    case _: S[n1] => 1 + toIntC[n1]

transparent inline def toIntT[N <: Nat]: Int =
  inline scala.compiletime.erasedValue[N] match
    case _: Zero.type => 0
    case _: Succ[n]   => toIntT[n] + 1

inline val ctwo = toIntC[2]

inline val UINT4: 4 = toIntC[4]
UINT4
val UINT8: 8        = 8
val UINT16: 16      = 16
val UINT32: 32      = 32

val foo                   = 23
def id[T](t: T): T        = t
id(23)
id(UINT4)
val size                  = identity[UINT32.type]
inline def identity[T]: T = {
  inline erasedValue[T] match {
    case _: T => constValue[T]
  }
}

case class Foo[T <: Int](desc: String) {
  val size = valueOf[T]
}

val small = Foo[UINT4.type]("This is a uint 4")
small.size

val big = Foo[UINT32.type]("This is a uint 32")
big.size
