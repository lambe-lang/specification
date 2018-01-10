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

  "trait Boolean { def (||) -> Boolean }" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean }").successful shouldBe true
  }

  "trait Boolean { def (||) -> Boolean }" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean }").get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(
          ValueType("||", List(), "Boolean")
        )
      )
  }

  "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }").successful shouldBe true
  }

  "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }").get shouldBe
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
