package org.lambe.lang.syntax

import org.scalatest._

class EntityParsersSpec extends FlatSpec with EntityParser with Matchers {
  // full code parsing

  val booleanCode: String =
    """
      |data Bool -> type
      |data true -> Bool
      |data false -> Bool
      |
      |trait Predicate {
      |  def (||) Bool -> Bool
      |  def (&&) Bool -> Bool
      |  def not  -> Bool
      |}
      |
      |define Predicate for Bool {
      |  def self(true)  (||) _ = self
      |  def self(false) (||) b = b
      |
      |  def self(false) (&&) _ = self
      |  def self(true)  (&&) b = b
      |
      |  def self(true)  not    = false
      |  def self(false) not    = true
      |}
    """.stripMargin

  "booleanCode" should "be parsed" in {
    parseAll(entities, booleanCode).successful shouldBe true
  }

}
