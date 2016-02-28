package se.gigurra.testsimulacrum

import org.scalatest._
import simulacrum._

import scala.language.higherKinds
import scala.language.implicitConversions

class TestSimulacrum
  extends WordSpec
    with Matchers
    with OneInstancePerTest {

  "Simulacrum" should {

    "make type class creation easier #2" in {

      case class Vector(x: Int, y: Int)
      implicit def tuple2Vector(t: (Int, Int)): Vector = (Vector.apply _).tupled(t)

      @typeclass trait MathOps[A] {
        @op("+") def add(x: A, y: A): A
        @op("*") def mult(x: A, y: A): A
        @op("*") def mult(x: A, y: Int): A
      }

      implicit val vectorAddition = new MathOps[Vector] {
        def add(a: Vector, b: Vector): Vector = Vector(a.x + b.x, a.y + b.y)
        def mult(a: Vector, b: Vector): Vector = Vector(a.x * b.x, a.y * b.y)
        def mult(v: Vector, s: Int): Vector = Vector(v.x * s, v.y * s)
      }

      import MathOps.ops._

      val v1 = Vector(1,2)
      val v2 = Vector(3,4)

      (v1 + v2) * 3 * (2,1) shouldBe Vector(24,18)

    }

  }

}
