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

class TraitParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  "trait Boolean" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean {}").successful shouldBe true
  }

  "trait Boolean" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean {}").get shouldBe TraitEntity("Boolean", List(), List())
  }

  "trait Boolean {}" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean {}").successful shouldBe true
  }

  "trait Boolean {}" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean {}").get shouldBe TraitEntity("Boolean", List(), List())
  }

  "trait Boolean { def (||) -> Boolean }" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean }").successful shouldBe true
  }

  "trait Boolean { def (||) -> Boolean }" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean }").get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(
          ValueType("||", List(), "Boolean")
        )
      )
  }

  "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }" should "be parsed" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }").successful shouldBe true
  }

  "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }" should "be a TraitEntity" in {
    parseAll(traitExpression, "trait Boolean { def (||) -> Boolean def (&&) -> Boolean }").get shouldBe
      TraitEntity(
        "Boolean",
        List(),
        List(
          ValueType("||", List(), "Boolean"),
          ValueType("&&", List(), "Boolean")
        )
      )
  }

}
