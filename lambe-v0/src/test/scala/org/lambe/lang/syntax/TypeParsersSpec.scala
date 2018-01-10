package org.lambe.lang.syntax

import org.scalatest._

class TypeParsersSpec extends FlatSpec with TypeParser with Matchers {
  // type parsing

  "type" should "be parsed" in {
    parseAll(simpleTypeExpression, "type").successful shouldBe true
  }

  "type" should "be parsed as a " in {
    parseAll(simpleTypeExpression, "type").get shouldBe TypeIdentifier("type")
  }

  "a" should "be parsed" in {
    parseAll(simpleTypeExpression, "type").successful shouldBe true
  }

  "a" should "be parsed as a " in {
    parseAll(simpleTypeExpression, "a").get shouldBe TypeIdentifier("a")
  }

  "a'" should "be parsed" in {
    parseAll(simpleTypeExpression, "type").successful shouldBe true
  }

  "a'" should "be parsed as a " in {
    parseAll(simpleTypeExpression, "a'").get shouldBe TypeIdentifier("a'")
  }

  "a type" should "be parsed" in {
    parseAll(appliedTypeExpression, "a type").successful shouldBe true
  }

  "a type" should "be parsed as a TypeApplication" in {
    parseAll(appliedTypeExpression, "a type").get shouldBe TypeApplication("a", "type")
  }

  "a -> b" should "be parsed" in {
    parseAll(typeExpression, "a -> b").successful shouldBe true
  }

  "a -> b" should "be parsed as a TypeAbstraction" in {
    parseAll(typeExpression, "a -> b").get shouldBe TypeAbstraction("a", "b")
  }

  "m a -> b" should "be parsed" in {
    parseAll(typeExpression, "m a -> b").successful shouldBe true
  }

  "m a -> b" should "be parsed as a TypeAbstraction" in {
    parseAll(typeExpression, "m a -> b").get shouldBe TypeAbstraction(TypeApplication("m", "a"), "b")
  }

  "m (a -> b)" should "be parsed" in {
    parseAll(typeExpression, "m (a -> b)").successful shouldBe true
  }

  "m (a -> b)" should "be parsed as a TypeApplication" in {
    parseAll(typeExpression, "m (a -> b)").get shouldBe TypeApplication("m", TypeAbstraction("a", "b"))
  }
}
