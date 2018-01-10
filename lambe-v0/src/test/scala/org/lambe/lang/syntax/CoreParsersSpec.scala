package org.lambe.lang.syntax

import org.scalatest._

class CoreParsersSpec extends FlatSpec with CoreParser with Matchers {
  // Number parsing

  "-12" should "be parsed" in {
    parseAll(numberLiteral, "-12").successful shouldBe true
  }

  "-12" should "be number -12" in {
    parseAll(numberLiteral, "-12").get shouldBe -12
  }

  "-1a2" should "not be parsed" in {
    parseAll(numberLiteral, "-1a2").successful shouldBe false
  }

  // Identifier Parsing

  "a12" should "be parsed" in {
    parseAll(identifier, "a12").successful shouldBe true
  }

  "a12" should "be string a12" in {
    parseAll(identifier, "a12").get shouldBe "a12"
  }

  "a12'" should "be string a12'" in {
    parseAll(identifier, "a12'").get shouldBe "a12'"
  }

  // Operator Parsing

  ">>=" should "be parsed" in {
    parseAll(operator, ">>=").successful shouldBe true
  }

  ">>=" should "be string >>=" in {
    parseAll(operator, ">>=").get shouldBe ">>="
  }


  // Unit Parsing

  "()" should "be parsed" in {
    parseAll(unit, "()").successful shouldBe true
  }

}
