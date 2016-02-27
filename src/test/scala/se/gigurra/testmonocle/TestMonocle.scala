package se.gigurra.testmonocle

import monocle.Lens
import monocle.macros.Lenses
import org.scalatest._
import org.scalatest.mock._

import scala.language.higherKinds

class TestMonocle
  extends WordSpec
    with MockitoSugar
    with Matchers
    with OneInstancePerTest {

  /**
    * This is awesome! :)
    */
  "Monocle" should {

    @Lenses case class Address(street : String, city : String, postcode : String)
    @Lenses case class Person(name : String, age : Int, address : Address)

    "make some lenses!" in {

      val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
      val nameLens = Person.name
      val cityLens = Person.address ^|-> Address.city // or Person.address.composeLens(Address.city)

      val truncatedJoe = nameLens.modify(_.dropRight(3))(person)
      val uptownJoe = cityLens.modify(_.toUpperCase)(person)

      truncatedJoe shouldBe Person("Joe G", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
      uptownJoe shouldBe Person("Joe Grey", 37, Address("Southover Street", "BRIGHTON", "BN2 9UA"))

    }

    "Use shortcut when making lenses!" in {

      implicit class RichLens[ObjectType, FieldType](lens: Lens[ObjectType, FieldType]) {
        def >[NextFieldType](nextLens: Lens[FieldType, NextFieldType]) = lens ^|-> nextLens
      }

      val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
      val cityLens = Person.address > Address.city
      val uptownJoe = cityLens.modify(_.toUpperCase)(person)
      uptownJoe shouldBe Person("Joe Grey", 37, Address("Southover Street", "BRIGHTON", "BN2 9UA"))

    }

    "Use fancy LensExtender hack - available if companion objects were easy to summon :(" in {
      import LensExtender._

      @Lenses case class Address2(street : String, city : String, postcode : String)
      @Lenses case class Person2(name : String, age : Int, address : Address2)

      // If these could be generated with macros..
      object Address2 {
        implicit def aComp = new Companion[Address2] {
          type C = Address2.type
          def apply() = Address2
        }
      }
      object Person2 {
        implicit def pComp = new Companion[Person2] {
          type C = Person2.type
          def apply() = Person2
        }
      }

      // Then we could do this!
      val p0 = Person2("Joe Grey", 37, Address2("Southover Street", "Brighton", "BN2 9UA"))
      val p1 = p0.set(_.name, "123")
      val p2 = p0.set(_.address(_.city), "dumbletown")

      p1 shouldBe Person2("123", 37, Address2("Southover Street", "Brighton", "BN2 9UA"))
      p2 shouldBe Person2("Joe Grey", 37, Address2("Southover Street", "dumbletown", "BN2 9UA"))

    }

  }

}
