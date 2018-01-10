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
        "Boolean"
      )
  }

  "val (>>=) [b:type](a -> m b) -> m b" should "be parsed" in {
    parseAll(valueType, "val (>>=) [b:type] (a -> m b) -> m b").successful shouldBe true
  }

  "val (>>=) [b:type](a -> m b) -> m b" should "be a ValueType" in {
    parseAll(valueType, "val (>>=) [b:type] (a -> m b) -> m b").get shouldBe
      ValueType(
        ">>=",
        List(("b", TypeIdentifier("type"))),
        TypeAbstraction(TypeAbstraction("a", TypeApplication("m", "b")), TypeApplication("m", "b"))
      )
  }

}
