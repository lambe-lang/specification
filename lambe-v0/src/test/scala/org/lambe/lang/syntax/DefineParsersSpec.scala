package org.lambe.lang.syntax

import org.scalatest._

class DefineParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  "define Boolean for Bool { def self(true) (||) = false def self(false) (||) = self }" should "be parsed" in {
    parseAll(defineExpression, "define Boolean for Bool { def self(true) (||) = false def self(false) (||) = self }").successful shouldBe true
  }

  "define Boolean for Bool { def self(true) (||) _ = self def self(false) (||) a = a }" should "be DefineEntity" in {
    parseAll(defineExpression, "define Boolean for Bool { def self(true) (||) _ = self def self(false) (||) a = a }").get shouldBe
      DefineEntity(
        "Boolean",
        List(),
        "Bool",
        List(
          ValueExpression(
            "||",
            Option("true"),
            List(),
            ExpressionAbstraction("_","self")
          ),
          ValueExpression(
            "||",
            Option("false"),
            List(),
            ExpressionAbstraction("a","a")
          )
        )
      )
  }

}
