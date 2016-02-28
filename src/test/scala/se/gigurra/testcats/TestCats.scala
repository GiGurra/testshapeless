package se.gigurra.testcats

import cats.std.all._
import cats.syntax.eq._
import org.scalatest._

import scala.language.higherKinds

class TestCats
  extends WordSpec
    with Matchers
    with OneInstancePerTest {

  /**
    * Tutorial available at http://eed3si9n.com/herding-cats/
    *
    * !!!!!!!!!!!!!!
    * Just like Scalaz - Cats === operator conflicts with scalatest.Suite's implementation
    * Unlike Scalaz - Cats offers no unicode substitute :/
    */
  "Cats" should {

    /**
      * !!!!!!!!!!!!!!
      * Just like Scalaz - Cats === operator conflicts with scalatest.Suite's implementation
      * Unlike Scalaz - Cats offers no unicode substitute :/
      */
    "have typesafe equals" in {
      Doh()
    }
  }

}

case class Doh() {
  1.0f === 1.0f
}
