package com.odenzo.xrpl.common.collections

trait Isomorphic[A, B] {
  def to(a: A): B
  def from(b: B): A
}
