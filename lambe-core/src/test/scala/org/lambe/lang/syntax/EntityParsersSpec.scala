package org.lambe.lang.syntax

import org.scalatest._

class EntityParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  "data Bool:type" should "be parsed" in {
    parseAll(dataExpression, "data Bool:type").successful shouldBe true
  }

  "data Bool:type" should "be a DataEntity" in {
    parseAll(dataExpression, "data Bool:type").get shouldBe DataEntity("Bool", List(), List(), TypeIdentifier("type"))
  }

  "data List[a:type]:type" should "be parsed" in {
    parseAll(dataExpression, "data List[a:type]:type").successful shouldBe true
  }

  "data List[a:type]:type" should "be a DataEntity" in {
    parseAll(dataExpression, "data List[a:type]:type").get shouldBe DataEntity("List", List(("a", TypeIdentifier("type"))), List(), TypeIdentifier("type"))
  }

  "data (::)[a:type](a)(List a):type" should "be parsed" in {
    parseAll(dataExpression, "data (::)[a:type](a)(List a):type").successful shouldBe true
  }

  "data (::)[a:type](a)(List a): List a" should "be a DataEntity" in {
    parseAll(dataExpression, "data (::)[a:type](a)(List a):List a").get shouldBe
      DataEntity("::", List(("a", TypeIdentifier("type"))), List(TypeIdentifier("a"), TypeApplication(TypeIdentifier("List"), TypeIdentifier("a"))), TypeApplication(TypeIdentifier("List"), TypeIdentifier("a")))
  }

}
