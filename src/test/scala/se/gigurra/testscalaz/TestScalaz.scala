package se.gigurra.testscalaz

import org.scalatest._

import scala.language.higherKinds

class TestScalaz
  extends WordSpec
    with Matchers
    with OneInstancePerTest {

  "Scalaz" should {

    import scalaz._
    import Scalaz._

    // Have to use ≟ instead of === here since stupid scalatest overrides === (&¤#!)

    "have typesafe equals" in {

      val someValue = Option(1.0f)
      someValue ≟ Some(1.0f) /* shouldBe true*/

      "1 ≟ 2.0f" shouldNot compile
      "1 ≟ 1" should compile

      "Option(123) ≟ Option(\"123\")" shouldNot compile
      "Option(123) ≟ Option(123)" should compile

    }

    "have better syntax for .getOrElse" in {
      val ok = Option("Yay")
      val fail = Option(null: String)

      ok ≠ fail shouldBe true

      ok.get ≟ fail.getOrElse("Yay") shouldBe true
      ok.get ≟ (fail | "Yay") shouldBe true
      ok.get ≟ (fail | "nah") shouldBe false

    }

  }

}
