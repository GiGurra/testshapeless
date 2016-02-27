package se.gigurra.testshapeless

import org.scalatest._
import org.scalatest.mock._

import scala.util.{Failure, Success, Try}
import shapeless._

/**
 * Examples taken from shapeless 2.0 feature list - where intellij fails miserably to analyze the code :(
 * All tests compile and work fine with sbt/scalac :S
 */
class ShapelessSpec
  extends WordSpec
    with MockitoSugar
    with Matchers
    with OneInstancePerTest {

  "Shapeless" should {

    "hlists -> scala lists" in {

      sealed trait Base {def foo(): Unit = { println("lo")}}
      case class D1() extends Base
      case class D2() extends Base

      import poly._
      val l = (23 :: "foo" :: HNil) :: HNil :: (true :: HNil) :: HNil

      println(l)

      val l2 = D2() :: D1() :: HNil
      val ls: Seq[Base] = l2.toList // INTELLIJ FAILS - Reports type mismatch/Cannot deduce result

      ls.foreach(_.foo())
    }

    "lenses" in {
      case class Address(street : String, city : String, postcode : String)
      case class Person(name : String, age : Int, address : Address)
      val nameLens: Lens[Person, String]     = lens[Person] >> 'name // INTELLIJ FAILS - Reports type mismatch/Cannot deduce result
      val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
      nameLens.set(person)("123")
    }

    "case class -> hlists -> case class | scala lists" in {
      case class Apple(i: Int, s: String, b: Boolean)
      val appleGen = Generic[Apple]

      val apple = Apple(23, "foo", true)
      val hlistApple: Int :: String :: Boolean :: HNil = appleGen.to(apple) // INTELLIJ FAILS - Reports type mismatch/Cannot deduce result

      println(hlistApple)

      val appleBack: Apple = appleGen.from(hlistApple)
      val scalaList: Seq[Any] = hlistApple.toList

      apple shouldBe appleBack
    }
    
    "Polymorphic function values" in {
      // This one actually works in intellij!

      import poly._

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

    "Coproducts as type/message constraints" in {

      case class Ok()
      case class ErrorType1()
      case class ErrorType2()

      type Result = Ok :+: ErrorType1 :+: ErrorType2 :+: CNil

      def op1 = Coproduct[Result](Ok())
      def op2 = Coproduct[Result](ErrorType1())
      def op3 = Coproduct[Result](ErrorType2())

      object handler extends Poly1 {
        implicit def caseOk   = at[Ok]        (x => 1)
        implicit def caseErr1 = at[ErrorType1](x => 2)
        implicit def caseErr2 = at[ErrorType2](x => 3)
      }

      println(op3.map(handler))

      op2.select[Ok]

      // extrapolated from: http://stackoverflow.com/questions/34107849/pattern-matching-with-shapeless-coproduct
      Seq(op1, op2, op3) foreach {
        case Inl(a)           => println("These aren't")
        case Inr(Inl(b))      => println("the droids")
        case Inr(Inr(Inl(b))) => println("you're looking for ..")
        case Inr(Inr(Inr(_))) => // Impossible - to make compile happy..
      }

    }

  }

}

