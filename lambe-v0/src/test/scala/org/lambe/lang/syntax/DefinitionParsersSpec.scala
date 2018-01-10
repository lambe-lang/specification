package org.lambe.lang.syntax

import org.scalatest._

class DefinitionParsersSpec extends FlatSpec with DefinitionParser with Matchers {
  // definition type parsing

  "def (||) -> Boolean" should "be parsed" in {
    parseAll(definitionType, "def (||) -> Boolean").successful shouldBe true
  }

  "def (||) -> Boolean" should "be a ValueType" in {
    parseAll(definitionType, "def (||) -> Boolean").get shouldBe
      ValueType(
        "||",
        List(),
        "Boolean"
      )
  }

  "def (>>=) [b:type] (a -> m b) -> m b" should "be parsed" in {
    parseAll(definitionType, "def (>>=) [b:type] (a -> m b) -> m b").successful shouldBe true
  }

  "def (>>=) [b:type] (a -> m b) -> m b" should "be a ValueType" in {
    parseAll(definitionType, "def (>>=) [b:type] (a -> m b) -> m b").get shouldBe
      ValueType(
        ">>=",
        List(("b", TypeIdentifier("type"))),
        TypeAbstraction(TypeAbstraction("a", TypeApplication("m", "b")), TypeApplication("m", "b"))
      )
  }

  // definition expression parsing

  "def (>>=) [b:type] a b = v" should "be parsed" in {
    parseAll(definitionExpression, "def (>>=) [b:type] a b = v").successful shouldBe true
  }

  "def (>>=) [b:type] a b = v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def (>>=) [b:type] a b = v").get shouldBe
      ValueExpression(
        ">>=",
        Option.empty,
        List(("b", TypeIdentifier("type"))),
        ExpressionAbstraction("a",ExpressionAbstraction("b", "v"))
      )
  }

  "def (>>=) [b:type] (a b) = v" should "be parsed" in {
    parseAll(definitionExpression, "def (>>=) [b:type] (a b) = v").successful shouldBe true
  }

  "def (>>=) [b:type] (a b) = v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def (>>=) [b:type] (a b) = v").get shouldBe
      ValueExpression(
        ">>=",
        Option.empty,
        List(("b", TypeIdentifier("type"))),
        ExpressionAbstraction(PatternApplication("a","b"), "v")
      )
  }

  "def (>>=) [b:type] = (a b) -> v" should "be parsed" in {
    parseAll(definitionExpression, "def (>>=) [b:type] = (a b) -> v").successful shouldBe true
  }

  "def (>>=) [b:type] = (a b) -> v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def (>>=) [b:type] = (a b) -> v").get shouldBe
      ValueExpression(
        ">>=",
        Option.empty,
        List(("b", TypeIdentifier("type"))),
        ExpressionAbstraction(PatternApplication("a","b"), "v")
      )
  }

  "def self(c d) (>>=) [b:type] = (a b) -> v" should "be parsed" in {
    parseAll(definitionExpression, "def self(c d) (>>=) [b:type] = (a b) -> v").successful shouldBe true
  }

  "def self(c d) (>>=) [b:type] = (a b) -> v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def self(c d) (>>=) [b:type] = (a b) -> v").get shouldBe
      ValueExpression(
        ">>=",
        Option(PatternApplication("c","d")),
        List(("b", TypeIdentifier("type"))),
        ExpressionAbstraction(PatternApplication("a","b"), "v")
      )
  }
}
