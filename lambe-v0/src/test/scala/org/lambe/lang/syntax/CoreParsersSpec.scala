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

class CoreParsersSpec extends FlatSpec with CoreParser with Matchers {
  // Number parsing

  "-12" should "be parsed" in {
    parseAll(numberLiteral, "-12").successful shouldBe true
  }

  "-12" should "be number -12" in {
    parseAll(numberLiteral, "-12").get shouldBe -12
  }

  "-1a2" should "not be parsed" in {
    parseAll(numberLiteral, "-1a2").successful shouldBe false
  }

  // Identifier Parsing

  "a12" should "be parsed" in {
    parseAll(identifier, "a12").successful shouldBe true
  }

  "a12" should "be string a12" in {
    parseAll(identifier, "a12").get shouldBe "a12"
  }

  "a12'" should "be string a12'" in {
    parseAll(identifier, "a12'").get shouldBe "a12'"
  }

  "trait" should "not be parsed'" in {
    parseAll(identifier, "trait").successful shouldBe false
  }

  // Operator Parsing

  ">>=" should "be parsed" in {
    parseAll(operator, ">>=").successful shouldBe true
  }

  ">>=" should "be string >>=" in {
    parseAll(operator, ">>=").get shouldBe ">>="
  }

  "[" should "not be parsed" in {
    parseAll(operator, "[").successful shouldBe false
  }

  // Unit Parsing

  "()" should "be parsed" in {
    parseAll(unit, "()").successful shouldBe true
  }

}
