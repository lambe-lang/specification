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

  "def (>>=) a b = v" should "be parsed" in {
    parseAll(definitionExpression, "def (>>=) a b = v").successful shouldBe true
  }

  "def (>>=) [b:type] a b = v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def (>>=) a b = v").get shouldBe
      ValueExpression(
        ">>=",
        Option.empty,
        ExpressionAbstraction("a",ExpressionAbstraction("b", "v"))
      )
  }

  "def (>>=) [b:type] (a b) = v" should "be parsed" in {
    parseAll(definitionExpression, "def (>>=) (a b) = v").successful shouldBe true
  }

  "def (>>=) [b:type] (a b) = v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def (>>=) (a b) = v").get shouldBe
      ValueExpression(
        ">>=",
        Option.empty,
        ExpressionAbstraction(PatternApplication("a","b"), "v")
      )
  }

  "def (>>=) [b:type] = (a b) -> v" should "be parsed" in {
    parseAll(definitionExpression, "def (>>=) = (a b) -> v").successful shouldBe true
  }

  "def (>>=) [b:type] = (a b) -> v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def (>>=) = (a b) -> v").get shouldBe
      ValueExpression(
        ">>=",
        Option.empty,
        ExpressionAbstraction(PatternApplication("a","b"), "v")
      )
  }

  "def self(c d) (>>=) = (a b) -> v" should "be parsed" in {
    parseAll(definitionExpression, "def self(c d) (>>=) = (a b) -> v").successful shouldBe true
  }

  "def self(c d) (>>=) = (a b) -> v" should "be ValueExpression" in {
    parseAll(definitionExpression, "def self(c d) (>>=) = (a b) -> v").get shouldBe
      ValueExpression(
        ">>=",
        Option(PatternApplication("c","d")),
        ExpressionAbstraction(PatternApplication("a","b"), "v")
      )
  }
}
