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

import org.lambe.lang.syntax._
import org.scalatest._

class DefineParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  private val value0 = "define Bool { def (||) true _ = true def (||) false a = a }"

  value0 should "be parsed" in {
    parseAll(defineExpression, value0).successful shouldBe true
  }

  value0 should "be DefineEntity" in {
    parseAll(defineExpression, value0).get shouldBe
      DefineEntity(
        List(),
        "Bool",
        List(),
        (
          List(
            ValueExpression(
              "||",
              ExpressionAbstraction("true", ExpressionAbstraction("_", "true"))
            ),
            ValueExpression(
              "||",
              ExpressionAbstraction("false", ExpressionAbstraction("a", "a"))
            )
          ),
          List()
        )
      )
  }

  private val value2 = "define Arithmetic Peano { def (+) Zero p = p }"

  value2 should "be parsed" in {
    parseAll(defineExpression, value2).successful shouldBe true
  }

  value2 should "be DefineEntity" in {
    parseAll(defineExpression, value2).get shouldBe
      DefineEntity(
        List(),
        TypeApplication("Arithmetic", "Peano"),
        List(),
        (
          List(
            ValueExpression(
              "+",
              ExpressionAbstraction("Zero",ExpressionAbstraction("p", "p"))
            )
          ),
          List(),
        )
      )
  }

  private val value3 = "define [a] Functor (Option a) { def fmap None _ = None def fmap (Some v) f = Some (f v) }"

  value3 should "be parsed" in {
    parseAll(defineExpression, value3).successful shouldBe true
  }

  value3 should "be DefineEntity" in {
    parseAll(defineExpression, value3).get shouldBe
      DefineEntity(
        List(("a", "type")),
        TypeApplication("Functor", TypeApplication("Option", "a")),
        List(),
        (
          List(
            ValueExpression(
              "fmap",
              ExpressionAbstraction("None",ExpressionAbstraction("_", "None"))
            ),
            ValueExpression(
              "fmap",
              ExpressionAbstraction(PatternApplication("Some", "v"),ExpressionAbstraction("f", ExpressionApplication("Some", ExpressionApplication("f", "v"))))
            )
          ),
          List()
        )
      )
  }

  private val value4 = "define A { data B : type }"

  value4 should "be parsed" in {
    parseAll(defineExpression, value4).successful shouldBe true
  }

  value4 should "be DefineEntity" in {
    parseAll(defineExpression, value4).get shouldBe
      DefineEntity(
        List(),
        "A",
        List(),
        (
          List(),
          List(DataEntity("B", List(), "type"))
        )
      )
  }

  private val value5 = "define A { data B : type data C : type }"

  value5 should "be parsed" in {
    parseAll(defineExpression, value5).successful shouldBe true
  }

  value5 should "be DefineEntity" in {
    parseAll(defineExpression, value5).get shouldBe
      DefineEntity(
        List(),
        "A",
        List(),
        (
          List(),
          List(DataEntity("B", List(), "type"), DataEntity("C", List(), "type"))
        )
      )
  }

  private val value6 = "define A { trait B }"

  value6 should "be parsed" in {
    parseAll(defineExpression, value6).successful shouldBe true
  }

  value6 should "be DefineEntity" in {
    parseAll(defineExpression, value6).get shouldBe
      DefineEntity(
        List(),
        "A",
        List(),
        (
          List(),
          List(TraitEntity("B", List(), List(), (List(), List())))
        )
      )
  }


  private val value7 = "define A { define B {} }"

  value7 should "be parsed" in {
    parseAll(defineExpression, value7).successful shouldBe true
  }

  value7 should "be DefineEntity" in {
    parseAll(defineExpression, value7).get shouldBe
      DefineEntity(
        List(),
        "A",
        List(),
        (
          List(),
          List(DefineEntity(List(), "B", List(), (List(), List())))
        )
      )
  }

  private val value8 = "define A with C {}"

  value8 should "be parsed" in {
    parseAll(defineExpression, value8).successful shouldBe true
  }

  value8 should "be DefineEntity" in {
    parseAll(defineExpression, value8).get shouldBe
      DefineEntity(
        List(),
        "A",
        List("C"),
        (
          List(),
          List()
        )
      )
  }
}
