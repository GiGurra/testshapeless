package se.gigurra.testmonocle

/**
  * Trick by Miles Sabin
  */
trait Companion[T] {
  type C <: AnyRef
  def apply() : C
}

object Companion {
  implicit def getCompanion[T](implicit comp : Companion[T]) = comp()
}
