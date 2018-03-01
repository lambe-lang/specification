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

class TypeParsersSpec extends FlatSpec with TypeParser with Matchers with Coercions {
  // type parsing

  "type" should "be parsed" in {
    parseAll(simpleTypeExpression, "type").successful shouldBe true
  }

  "type" should "be parsed as a " in {
    parseAll(simpleTypeExpression, "type").get shouldBe TypeIdentifier("type")
  }

  "a" should "be parsed" in {
    parseAll(simpleTypeExpression, "type").successful shouldBe true
  }

  "a" should "be parsed as a " in {
    parseAll(simpleTypeExpression, "a").get shouldBe TypeIdentifier("a")
  }

  "a'" should "be parsed" in {
    parseAll(simpleTypeExpression, "type").successful shouldBe true
  }

  "a'" should "be parsed as a " in {
    parseAll(simpleTypeExpression, "a'").get shouldBe TypeIdentifier("a'")
  }

  "a b" should "be parsed" in {
    parseAll(appliedTypeExpression, "a b").successful shouldBe true
  }

  "a b" should "be parsed as a TypeApplication" in {
    parseAll(appliedTypeExpression, "a b").get shouldBe TypeApplication("a", "b")
  }

  "a b c" should "be parsed" in {
    parseAll(appliedTypeExpression, "a b c").get shouldBe TypeApplication(TypeApplication("a", "b"), "c")
  }

  "a b c" should "be parsed as a TypeApplication" in {
    parseAll(appliedTypeExpression, "a b c").successful shouldBe true
  }

  "a -> b" should "be parsed" in {
    parseAll(typeExpression, "a -> b").successful shouldBe true
  }

  "a -> b" should "be parsed as a TypeAbstraction" in {
    parseAll(typeExpression, "a -> b").get shouldBe TypeAbstraction("a", "b")
  }

  "m a -> b" should "be parsed" in {
    parseAll(typeExpression, "m a -> b").successful shouldBe true
  }

  "m a -> b" should "be parsed as a TypeAbstraction" in {
    parseAll(typeExpression, "m a -> b").get shouldBe TypeAbstraction(TypeApplication("m", "a"), "b")
  }

  "m (a -> b)" should "be parsed" in {
    parseAll(typeExpression, "m (a -> b)").successful shouldBe true
  }

  "m (a -> b)" should "be parsed as a TypeApplication" in {
    parseAll(typeExpression, "m (a -> b)").get shouldBe TypeApplication("m", TypeAbstraction("a", "b"))
  }

  "(a:type) -> a" should "be parsed" in {
    parseAll(typeExpression, "(a:type) -> a").successful shouldBe true
  }

  "(a:type) -> a" should "be parsed as a TypeAbstraction" in {
    parseAll(typeExpression, "(a:type) -> a").get shouldBe TypeForall("a", "type", "a")
  }
}
