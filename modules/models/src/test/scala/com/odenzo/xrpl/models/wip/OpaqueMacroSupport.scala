package com.odenzo.xrpl.models.wip









//package com.odenzo.xrpl.models.atoms.tobeopaque
//
//import scala.quoted.*
//import scodec.bits.ByteVector
//
//val foo: Type[ByteVector] = Type.of[ByteVector]
//
//def singletonListExpr[T: Type](x: Expr[T])(using Quotes): Expr[List[T]] =
//  '{ List[T]($x) } // generic T used within a quote
//
//def emptyListExpr[T](using Type[T], Quotes): Expr[List[T]] =
//  '{ List.empty[T] } // generic T used within a quote
//
//def unrolledPowerCode(x: Expr[ByteVector], len: Int, code:Byte)(using Quotes): Expr[Double] =
//
//
//
//def '[T](x: T): Quotes ?=> Expr[T] // def '[T](x: T)(using Quotes): Expr[T]
//
//def $[T](x: Quotes ?=> Expr[T]): T
