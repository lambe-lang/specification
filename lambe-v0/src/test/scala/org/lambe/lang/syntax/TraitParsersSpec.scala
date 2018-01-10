package org.lambe.lang.syntax

import org.scalatest._

class TraitParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  "trait Boolean" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean {}").successful shouldBe true
  }

  "trait Boolean" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean {}").get shouldBe TraitEntity("Boolean", List(), List())
  }

  "trait Boolean {}" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean {}").successful shouldBe true
  }

  "trait Boolean {}" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean {}").get shouldBe TraitEntity("Boolean", List(), List())
  }

  "trait Boolean { val (||) -> Boolean }" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean { val (||) -> Boolean }").successful shouldBe true
  }

  "trait Boolean { val (||) -> Boolean }" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean { val (||) -> Boolean }").get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(
          ValueType("||", List(), "Boolean")
        )
      )
  }

  "trait Boolean { val (||) -> Boolean val (&&) -> Boolean }" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean { val (||) -> Boolean val (&&) -> Boolean }").successful shouldBe true
  }

  "trait Boolean { val (||) -> Boolean val (&&) -> Boolean }" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean { val (||) -> Boolean val (&&) -> Boolean }").get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(
          ValueType("||", List(), "Boolean"),
          ValueType("&&", List(), "Boolean")
        )
      )
  }

}
