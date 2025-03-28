package com.odenzo.xrpl.common.utils

object ErrorUtils {
  def raiseError[E <: Throwable, T](attempt: Either[E, T]): T =
    attempt match
      case Left(value)  => throw value
      case Right(value) => value

  extension [E <: Throwable, T](errorEither: Either[E, T]) def raise: T = raiseError(errorEither)
}
