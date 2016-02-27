package se.gigurra.testshapeless

import org.scalatest._
import org.scalatest.mock._

import scala.util.{Failure, Success, Try}
import shapeless._

class ShapelessSpec
  extends WordSpec
    with MockitoSugar
    with Matchers
    with OneInstancePerTest {

  "Shapeless" should {

    "work" in {

      import poly._

      // choose is a function from Sets to Options with no type specific cases
      object choose extends (Set ~> Option) {
        def apply[T](s : Set[T]) = s.headOption
      }

      choose(Set(1, 2, 3))
      choose(Set('a', 'b', 'c'))

      object size extends Poly1 {
        implicit def caseInt = at[Int](x => 1)
        implicit def caseString = at[String](_.length)
        implicit def caseTuple[T, U]
        (implicit st : Case.Aux[T, Int], su : Case.Aux[U, Int]) =
          at[(T, U)](t => (size(t._1), size(t._2)))
      }

      println(size((1, "32")))
    }

    "hlists -> scala lists" in {

      sealed trait Base {def foo(): Unit = { println("lo")}}
      case class D1() extends Base
      case class D2() extends Base

      import poly._
      val l = (23 :: "foo" :: HNil) :: HNil :: (true :: HNil) :: HNil

      println(l)

      val l2 = D2() :: D1() :: HNil
      val ls: Seq[Base] = l2.toList

      ls.foreach(_.foo())
    }

    "lenses" in {

      // A pair of ordinary case classes ...
      case class Address(street : String, city : String, postcode : String)
      case class Person(name : String, age : Int, address : Address)

      // Some lenses over Person/Address ...
      val nameLens: Lens[Person, String]     = lens[Person] >> 'name
      val ageLens      = lens[Person] >> 'age
      val addressLens  = lens[Person] >> 'address
      val streetLens   = lens[Person] >> 'address >> 'street
      val cityLens     = lens[Person] >> 'address >> 'city
      val postcodeLens = lens[Person] >> 'address >> 'postcode

      val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))

      nameLens.set(person)("123")

    }

    "case class -> hlists -> case class | scala lists" in {
      case class Apple(i: Int, s: String, b: Boolean)
      val appleGen = Generic[Apple]

      val apple = Apple(23, "foo", true)
      val hlistApple: Int :: String :: Boolean :: HNil = appleGen.to(apple)

      println(hlistApple)

      val appleBack: Apple = appleGen.from(hlistApple)
      val scalaList: Seq[Any] = hlistApple.toList

      apple shouldBe appleBack
    }
  }

}

