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

class EntityParsersSpec extends FlatSpec with ModuleParser with Matchers {
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
      |module data.boolean
      |
      |data Bool : type
      |data true : Bool
      |data false : Bool
      |
      |trait Predicate {
      |  def (||) : Bool -> Bool
      |  def (&&) : Bool -> Bool
      |  def not  : Bool
      |}
      |
      |define Predicate {
      |  def (||) true  _ = true
      |  def (||) false b = b
      |
      |  def (&&) false _ = false
      |  def (&&) true  b = b
      |
      |  def not true  = false
      |  def not false = true
      |}
    """.stripMargin

  "booleanCode" should "be parsed" in {
    parseAll(module, booleanCode).successful shouldBe true
  }

  val listCode: String =
    """
      |module data.list
      |
      |data List : type -> type
      |data Nil  : [a] List a
      |data (::) : [a] a -> List a -> List a
      |
      |define Functor List {
      |  def fmap Nil    b f = Nil
      |  def fmap (h::t) b f = (f h) :: (t.map b f)
      |}
      |
      |define Reducer List {
      |  def reduce Nil    b _ = b
      |  def reduce (h::t) b f = t reduce (f b h) f
      |}
    """.stripMargin

  "listCode" should "be parsed" in {
    parseAll(module, listCode).successful shouldBe true
  }

  val listTrait: String =
    """
      |module list.build
      |
      |trait List (a) {
      |  def ([) : Parameter a
      |
      |  data Parameter : (List a -> List a) -> type
      |
      |  trait Parameter {
      |    def apply : (a) -> NextParameter a
      |    def (]) : List a
      |  }
      |
      |  data NextParameter : (List a -> List a) -> type
      |
      |  trait NextParameter {
      |    def (,) : a -> Parameter a
      |    def (]) : List a
      |  }
      |}
    """.stripMargin

  "listTrait" should "be parsed" in {
    parseAll(module, listTrait).successful shouldBe true
  }

  val listDefine: String =
    """
      |module list.build
      |
      |define [a] List a {
      |  def ([) = Parameter (l -> l)
      |
      |  data Parameter : (List a -> List a) -> type
      |
      |  trait Parameter {
      |    def apply : (a) -> NextParameter a
      |    def (]) : List a i
      |  }
      |
      |  data NextParameter : (List a -> List a) -> type
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
    parseAll(module, listDefine).successful shouldBe true
  }

  val listControl: String =
    """
      |module data.list
      |
      |data List : type -> type
      |data Nil  : [a] List a
      |data (::) : [a] a -> List a -> List a
      |
      |trait Searchable (m:type->type) (a) with Equatable a {
      |    def member : a -> m a -> Bool
      |}
      |
      |define [a] Searchable List a {
      |    def member _ Nil = false
      |    def member a (b::l) = a == b || member a l
      |}
      |
      |define (a) Adder (List a) {
      |    def (+) Nil    l = l
      |    def (+) (h::t) l = h :: (t + l)
      |}
    """.stripMargin

  "listControl" should "be parsed" in {
    parseAll(module, listControl).successful shouldBe true
  }

  val proFunctor: String =
    """
      |module data.profunctor
      |
      |data (::~>) : [a,b] (p:type->type->type) -> (q:type->type->type) -> p a b -> q a b
      |
      |trait HPFunctor (pp:(type->type->type)->type) {
      |  def hpmap  : (p ::~> q) -> (pp p ::~> pp q)
      |  def ddimap : (s -> a) -> (b -> t) -> pp p a b -> pp p s t
      |}
    """.stripMargin

  "proFunctor" should "be parsed" in {
    parseAll(module, proFunctor).successful shouldBe true
  }

}
