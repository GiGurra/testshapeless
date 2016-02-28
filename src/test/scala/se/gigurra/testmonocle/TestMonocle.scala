package se.gigurra.testmonocle

import monocle.Lens
import monocle.macros.Lenses
import org.scalatest._
import org.scalatest.mock._
import DirectLenses._

import scala.language.higherKinds

class TestMonocle
  extends WordSpec
    with MockitoSugar
    with Matchers
    with OneInstancePerTest {

  "Monocle" should {

    @Lenses case class Address(street: String, city: String, postcode: String)
    @Lenses case class Person(name: String, age: Int, address: Address)

    "make some lenses!" in {

      val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
      val nameLens = Person.name
      val cityLens = Person.address ^|-> Address.city // or Person.address.composeLens(Address.city)

      val truncatedJoe = nameLens.modify(_.dropRight(3))(person)
      val uptownJoe = cityLens.modify(_.toUpperCase)(person)

      truncatedJoe shouldBe Person("Joe G", 37, Address("Southover Street", "Brighton", "BN2 9UA"))
      uptownJoe shouldBe Person("Joe Grey", 37, Address("Southover Street", "BRIGHTON", "BN2 9UA"))

    }

    "Use fancy LensExtender hack" in {

      val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))

      person.set(_(_.address.city), "springfield")  shouldBe  person.copy(address = person.address.copy(city = "springfield"))
      person.set(_(_.name), "bob")                  shouldBe  person.copy(name = "bob")
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

  }

}
