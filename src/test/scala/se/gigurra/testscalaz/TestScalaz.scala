package se.gigurra.testscalaz

import org.scalatest._

import scala.language.higherKinds

class TestScalaz
  extends WordSpec
    with Matchers
    with OneInstancePerTest {

  "Scalaz" should {

    "Typesafe equals for options?" in {

      // Have to use ≟ here since stupid scalatest overrides === :S

      import scalaz._
      import Scalaz._

      val someValue = Option(1.0f)
      someValue ≟ Some(1.0f) /* shouldBe true*/

      "1 ≟ 2.0f" shouldNot compile
      "1 ≟ 1" should compile

      "Option(123) ≟ Option(\"123\")" shouldNot compile
      "Option(123) ≟ Option(123)" should compile

    }

  }

}
