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

class TraitParsersSpec extends FlatSpec with EntityParser with Matchers with Coercions {
  // entity parsing

  private val value0 = "trait Boolean"

  value0 should "be parsed" in {
    parseAll(traitExpression, value0).successful shouldBe true
  }

  value0 should "be a TraitEntity" in {
    parseAll(traitExpression, value0).get shouldBe TraitEntity("Boolean", List(), List(), (List(), List()))
  }

  private val value1 = "trait Boolean {}"

  value1 should "be parsed" in {
    parseAll(traitExpression, value1).successful shouldBe true
  }

  value1 should "be a TraitEntity" in {
    parseAll(traitExpression, value1).get shouldBe TraitEntity("Boolean", List(), List(), (List(), List()))
  }

  private val value2 = "trait Boolean { def (||) : Boolean }"

  value2 should "be parsed" in {
    parseAll(traitExpression, value2).successful shouldBe true
  }

  value2 should "be a TraitEntity" in {
    parseAll(traitExpression, value2).get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(),
        (
          List(
            ValueType("||", List(), "Boolean")
          ),
          List()
        )
      )
  }

  private val value3 = "trait Boolean { def (||) : Boolean def (&&) : Boolean }"

  value3 should "be parsed" in {
    parseAll(traitExpression, value3).successful shouldBe true
  }

  value3 should "be a TraitEntity" in {
    parseAll(traitExpression, value3).get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(),
        (
          List(
            ValueType("||", List(), "Boolean"),
            ValueType("&&", List(), "Boolean")
          ),
          List()
        )
      )
  }

  private val value4 = "trait Boolean { data true : Boolean }"

  value4 should "be parsed" in {
    parseAll(traitExpression, value4).successful shouldBe true
  }

  value4 should "be TraitEntity" in {
    parseAll(traitExpression, value4).get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(),
        (
          List(),
          List(
            DataEntity("true", List(), "Boolean")
          )
        )
      )
  }

  private val value5 = "trait Boolean { trait True {} }"

  value5 should "be parsed" in {
    parseAll(traitExpression, value5).successful shouldBe true
  }

  value5 should "be TraitEntity" in {
    parseAll(traitExpression, value5).get shouldBe
    TraitEntity(
      "Boolean",
      List(),
      List(),
      (
        List(),
        List(
          TraitEntity("True", List(), List(), (List(),List()))
        )
      )
    )
  }
}
