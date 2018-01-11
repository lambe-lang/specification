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

package org.lambe.lang.syntax

import org.scalatest._

class DefineParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  "define Bool { def self(true) (||) = false def self(false) (||) = self }" should "be parsed" in {
    parseAll(defineExpression, "define Bool { def self(true) (||) = false def self(false) (||) = self }").successful shouldBe true
  }

  "define Bool { def self(true) (||) _ = self def self(false) (||) a = a }" should "be DefineEntity" in {
    parseAll(defineExpression, "define Bool { def self(true) (||) _ = self def self(false) (||) a = a }").get shouldBe
      DefineEntity(
        List(),
        "Bool",
        "Bool",
        List(
          ValueExpression(
            "||",
            Option("true"),
            ExpressionAbstraction("_", "self")
          ),
          ValueExpression(
            "||",
            Option("false"),
            ExpressionAbstraction("a", "a")
          )
        )
      )
  }

  "define Boolean for Bool { def self(true) (||) = false def self(false) (||) = self }" should "be parsed" in {
    parseAll(defineExpression, "define Boolean for Bool { def self(true) (||) = false def self(false) (||) = self }").successful shouldBe true
  }

  "define Boolean for Bool { def self(true) (||) _ = self def self(false) (||) a = a }" should "be DefineEntity" in {
    parseAll(defineExpression, "define Boolean for Bool { def self(true) (||) _ = self def self(false) (||) a = a }").get shouldBe
      DefineEntity(
        List(),
        "Boolean",
        "Bool",
        List(
          ValueExpression(
            "||",
            Option("true"),
            ExpressionAbstraction("_", "self")
          ),
          ValueExpression(
            "||",
            Option("false"),
            ExpressionAbstraction("a", "a")
          )
        )
      )
  }

  "define Arithmetic Peano for Peano { def self(Zero) (+) p = p }" should "be parsed" in {
    parseAll(defineExpression, "define Arithmetic Peano for Peano { def self(Zero) (+) p = p }").successful shouldBe true
  }

  "define Arithmetic Peano for Peano { def self(Zero) (+) p = p }" should "be DefineEntity" in {
    parseAll(defineExpression, "define Arithmetic Peano for Peano { def self(Zero) (+) p = p }").get shouldBe
      DefineEntity(
        List(),
        TypeApplication("Arithmetic", "Peano"),
        "Peano",
        List(
          ValueExpression(
            "+",
            Option("Zero"),
            ExpressionAbstraction("p", "p")
          )
        )
      )
  }

  "define [a:type] Functor Option a for Option a { def self(None) fmap _ = None def self(Some v) fmap f = Some (f v) }" should "be parsed" in {
    parseAll(defineExpression, "define [a:type] Functor Option a for Option a when None { def self(None) fmap _ = None def self(Some v) fmap f = Some (f v) }").successful shouldBe true
  }

  "define [a:type] Functor Option a for Option a { def self(None) fmap _ = None def self(Some v) fmap f = Some (f v) }" should "be DefineEntity" in {
    parseAll(defineExpression, "define [a:type] Functor Option a for Option a { def self(None) fmap _ = None def self(Some v) fmap f = Some (f v) }").get shouldBe
      DefineEntity(
        List(("a", "type")),
        TypeApplication(TypeApplication("Functor", "Option"), "a"),
        TypeApplication("Option", "a"),
        List(
          ValueExpression(
            "fmap",
            Option("None"),
            ExpressionAbstraction("_", "None")
          ),
          ValueExpression(
            "fmap",
            Option(PatternApplication("Some", "v")),
            ExpressionAbstraction("f", ExpressionApplication("Some", ExpressionApplication("f" , "v")))
          )
        )
      )
  }

}
