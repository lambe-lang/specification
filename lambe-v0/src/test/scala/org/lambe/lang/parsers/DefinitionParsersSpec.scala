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

class DefinitionParsersSpec extends FlatSpec with DefinitionParser with Matchers {
  // definition type parsing

  private val value0 = "def (||) : Boolean"

  value0 should "be parsed" in {
    parseAll(definitionType, value0).successful shouldBe true
  }

  value0 should "be a ValueType" in {
    parseAll(definitionType, value0).get shouldBe
      ValueType(
        "||",
        List(),
        "Boolean"
      )
  }

  private val value1 = "def (a:type,b:type) (>>=) : (a -> m b) -> m b"

  value1 should "be parsed" in {
    parseAll(definitionType, value1).successful shouldBe true
  }

  value1 should "be a ValueType" in {
    parseAll(definitionType, value1).get shouldBe
      ValueType(
        ">>=",
        List(("a", TypeIdentifier("type")), ("b", TypeIdentifier("type"))),
        TypeAbstraction(Option.empty, TypeAbstraction(Option.empty, "a", TypeApplication("m", "b")), TypeApplication("m", "b"))
      )
  }

  private val value11 = "def (a,b) (>>=) : (a -> m b) -> m b"

  value11 should "be parsed" in {
    parseAll(definitionType, value11).successful shouldBe true
  }

  value11 should "be a ValueType" in {
    parseAll(definitionType, value11).get shouldBe
      ValueType(
        ">>=",
        List(("a", TypeIdentifier("type")), ("b", TypeIdentifier("type"))),
        TypeAbstraction(Option.empty, TypeAbstraction(Option.empty, "a", TypeApplication("m", "b")), TypeApplication("m", "b"))
      )
  }

  // definition expression parsing

  private val value2 = "def (>>=) a b = v"

  value2 should "be parsed" in {
    parseAll(definitionExpression, value2).successful shouldBe true
  }

  value2 should "be ValueExpression" in {
    parseAll(definitionExpression, value2).get shouldBe
      ValueExpression(
        ">>=",
        ExpressionAbstraction("a", ExpressionAbstraction("b", "v"))
      )
  }

  private val value3 = "def (>>=) (a b) = v"

  value3 should "be parsed" in {
    parseAll(definitionExpression, value3).successful shouldBe true
  }

  value3 should "be ValueExpression" in {
    parseAll(definitionExpression, value3).get shouldBe
      ValueExpression(
        ">>=",
        ExpressionAbstraction(PatternApplication("a", "b"), "v")
      )
  }

  private val value4 = "def (>>=) = (a b) -> v"

  value4 should "be parsed" in {
    parseAll(definitionExpression, value4).successful shouldBe true
  }

  value4 should "be ValueExpression" in {
    parseAll(definitionExpression, value4).get shouldBe
      ValueExpression(
        ">>=",
        ExpressionAbstraction(PatternApplication("a", "b"), "v")
      )
  }

}
