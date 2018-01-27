/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */

package org.lambe.lang.parsers

import org.scalatest._

class EntityParsersSpec extends FlatSpec with EntityParser with Matchers {
  // full code parsing

  val helloCode: String =
    """
      |// Simple Hello function
      |
      |def hello : String -> String
      |
      |def hello "world" as w = "Hello " + w + "!"
      |def hello name = "Hello " + name
    """.stripMargin

  "helloCode" should "be parsed" in {
    parseAll(entities, helloCode).successful shouldBe true
  }

  val factorialCode: String =
    """
      |// Recursive factorial definition
      |
      |def factorial : Int -> Int
      |
      |def factorial 0 = 1
      |def factorial n = n * $ factorial $ n - 1
    """.stripMargin

  "factorialCode" should "be parsed" in {
    parseAll(entities, factorialCode).successful shouldBe true
  }

  val booleanCode: String =
    """
      |data Bool : type
      |data true : Bool
      |data false : Bool
      |
      |trait Predicate {
      |  def (||) : Bool -> Bool
      |  def (&&) : Bool -> Bool
      |  def not : Bool
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

  val listCode: String =
    """
      |data List : type -> type
      |data (a) Nil : List a
      |data (a) (::) : a -> List a -> List a
      |
      |define (a) Functor (a -> List a) a for List a {
      |  def self(Nil)  fmap b f = Nil
      |  def self(h::t) fmap b f = (f h) :: (t.map b f)
      |}
      |
      |define (a) Reducer a for List a when Nil {
      |  def self(Nil)  reduce b _ = b
      |  def self(h::t) reduce b f = t reduce (f b h) f
      |}
    """.stripMargin

  "listCode" should "be parsed" in {
    parseAll(entities, listCode).successful shouldBe true
  }

  val listTrait: String =
    """
      |trait List (a) {
      |  def ([) : Parameter a
      |
      |  data (a) Parameter : (List a -> List a) -> type
      |
      |  trait Parameter {
      |    def apply : (a) -> NextParameter a
      |    def (]) : List a
      |  }
      |
      |  data (a) NextParameter : (List a -> List a) -> type
      |
      |  trait NextParameter {
      |    def (,) : a -> Parameter a
      |    def (]) : List a
      |  }
      |}
    """.stripMargin

  "listTrait" should "be parsed" in {
    parseAll(entities, listTrait).successful shouldBe true
  }

  val listDefine: String =
    """
      |define (a) List a {
      |  def ([) = Parameter (l -> l)
      |
      |  data (a) Parameter : (List a -> List a) -> type
      |
      |  trait Parameter {
      |    def apply : (a) -> NextParameter a
      |    def (]) : List a i
      |  }
      |
      |  data (a) NextParameter : (List a -> List a) -> type
      |
      |  trait NextParameter {
      |    def (,) : a -> Parameter a
      |    def (]) : List a
      |  }
      |
      |  define Parameter {
      |    def apply a = NextParameter (l -> f $ a :: l)
      |    def (])     = f Nil
      |  }
      |
      |  define NextParameter {
      |    def (,) = Parameter l
      |    def (]) = f Nil
      |  }
      |}
    """.stripMargin

  "listDefine" should "be parsed" in {
    parseAll(entities, listDefine).successful shouldBe true
  }
}
