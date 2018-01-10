package org.lambe.lang.syntax

import org.scalatest._

class ValueParsersSpec extends FlatSpec with BehaviorParser with Matchers {
  // value parsing

  "val (||) -> Boolean" should "be parsed" in {
    parseAll(valueType, "val (||) -> Boolean").successful shouldBe true
  }

  "val (||) -> Boolean" should "be a ValueType" in {
    parseAll(valueType, "val (||) -> Boolean").get shouldBe
      ValueType(
        "||",
        List(),
        List(),
        TypeIdentifier("Boolean")
      )
  }

  "val (>>=) [b:type](a -> m b) -> m b" should "be parsed" in {
    parseAll(valueType, "val (>>=) [b:type](a -> m b) -> m b").successful shouldBe true
  }

  "val (>>=) [b:type](a -> m b) -> m b" should "be a ValueType" in {
    parseAll(valueType, "val (>>=) [b:type](a -> m b) -> m b").get shouldBe
      ValueType(
        ">>=",
        List(("b", TypeIdentifier("type"))),
        List(TypeAbstraction(TypeIdentifier("a"), TypeApplication(TypeIdentifier("m"), TypeIdentifier("b")))),
        TypeApplication(TypeIdentifier("m"), TypeIdentifier("b"))
      )
  }

}
