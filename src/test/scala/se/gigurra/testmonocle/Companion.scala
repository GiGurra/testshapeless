package se.gigurra.testmonocle

trait Companion[T] {
  type C <: AnyRef
  def apply() : C
}

trait CC[T] {
  type C <: AnyRef
  def apply() : C
}

object Companion {
  implicit def getCompanion[T](implicit comp : Companion[T]) = comp()
}

/*
object TestCompanion {
trait Foo

object Foo {
  def bar = "wibble"

  // Per-companion boilerplate for access via implicit resolution
  implicit def companion = new Companion[Foo] {
    type C = Foo.type
    def apply() = Foo
  }
}

import Companion._

val fc = companion[Foo]  // Type is Foo.type
val s = fc.bar           // bar is accessible
}
*/
