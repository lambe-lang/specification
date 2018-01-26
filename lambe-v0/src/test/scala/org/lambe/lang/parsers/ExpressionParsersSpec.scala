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

class ExpressionParsersSpec extends FlatSpec with ExpressionParser with Matchers {
  // expression parsing

  "-12" should "be parsed" in {
    parseAll(expression, "-12").successful shouldBe true
  }

  "-12" should "be ExpressionInteger" in {
    parseAll(expression, "-12").get shouldBe ExpressionInteger(-12)
  }

  "42" should "be parsed" in {
    parseAll(expression, "42").successful shouldBe true
  }

  "42" should "be ExpressionInteger" in {
    parseAll(expression, "42").get shouldBe ExpressionInteger(42)
  }


  "\"-12\"" should "be parsed" in {
    parseAll(expression, "\"-12\"").successful shouldBe true
  }

  "\"-12\"" should "be ExpressionString" in {
    parseAll(expression, "\"-12\"").get shouldBe ExpressionString("-12")
  }

  "a" should "be parsed" in {
    parseAll(expression, "a").successful shouldBe true
  }

  "a" should "be ExpressionIdentifier" in {
    parseAll(expression, "a").get shouldBe ExpressionIdentifier("a")
  }

  "(a)" should "be parsed" in {
    parseAll(expression, "(a)").successful shouldBe true
  }

  "(a)" should "be ExpressionIdentifier" in {
    parseAll(expression, "(a)").get shouldBe ExpressionIdentifier("a")
  }

  "a b" should "be parsed" in {
    parseAll(expression, "a b").successful shouldBe true
  }

  "a b" should "be ExpressionApplication" in {
    parseAll(expression, "a b").get shouldBe ExpressionApplication("a", "b")
  }

  "(a b)" should "be parsed" in {
    parseAll(expression, "(a b)").successful shouldBe true
  }

  "(a b)" should "be ExpressionApplication" in {
    parseAll(expression, "(a b)").get shouldBe ExpressionApplication("a", "b")
  }

  "$ a b" should "be parsed" in {
    parseAll(expression, "$ a b").successful shouldBe true
  }

  "$ a b" should "be ExpressionApplication" in {
    parseAll(expression, "$ a b").get shouldBe ExpressionApplication("a", "b")
  }

  "a b c" should "be parsed" in {
    parseAll(expression, "a b c").successful shouldBe true
  }

  "a b c" should "be ExpressionApplication" in {
    parseAll(expression, "a b c").get shouldBe ExpressionApplication(ExpressionApplication("a", "b"), "c")
  }

  "a + c" should "be parsed" in {
    parseAll(expression, "a + c").successful shouldBe true
  }

  "a + c" should "be ExpressionApplication" in {
    parseAll(expression, "a + c").get shouldBe ExpressionApplication(ExpressionApplication("a", "+"), "c")
  }

  "a (b c)" should "be parsed" in {
    parseAll(expression, "a (b c)").successful shouldBe true
  }

  "a (b c)" should "be ExpressionApplication" in {
    parseAll(expression, "a (b c)").get shouldBe ExpressionApplication("a", ExpressionApplication("b", "c"))
  }

  "a $ b c" should "be parsed" in {
    parseAll(expression, "a $ b c").successful shouldBe true
  }

  "a $ b c" should "be ExpressionApplication" in {
    parseAll(expression, "a $ b c").get shouldBe ExpressionApplication("a", ExpressionApplication("b", "c"))
  }

  "a -> b" should "be parsed" in {
    parseAll(expression, "a -> b").successful shouldBe true
  }

  "a -> b" should "be ExpressionAbstraction" in {
    parseAll(expression, "a -> b").get shouldBe ExpressionAbstraction("a", "b")
  }

  "(a b) -> c" should "be parsed" in {
    parseAll(expression, "(a b) -> c").successful shouldBe true
  }

  "(a b) -> c" should "be ExpressionAbstraction" in {
    parseAll(expression, "(a b) -> c").get shouldBe ExpressionAbstraction(PatternApplication("a", "b"), "c")
  }

  "a -> b -> c" should "be parsed" in {
    parseAll(expression, "a -> b -> c").successful shouldBe true
  }

  "a -> b -> c" should "be ExpressionAbstraction" in {
    parseAll(expression, "a -> b -> c").get shouldBe ExpressionAbstraction("a", ExpressionAbstraction("b", "c"))
  }

  "a b -> c" should "be parsed" in {
    parseAll(expression, "a b -> c").successful shouldBe true
  }

  "a b -> c" should "be ExpressionAbstraction" in {
    parseAll(expression, "a b -> c").get shouldBe ExpressionAbstraction("a", ExpressionAbstraction("b", "c"))
  }

  "a $ b -> c" should "be parsed" in {
    parseAll(expression, "a b -> c").successful shouldBe true
  }

  "a $ b -> c" should "be ExpressionApplication" in {
    parseAll(expression, "a $ b -> c").get shouldBe ExpressionApplication("a", ExpressionAbstraction("b", "c"))
  }

  "let a = b in c" should "be parsed" in {
    parseAll(expression, "let a = b in c").successful shouldBe true
  }

  "let a = b in c" should "be ExpressionLet" in {
    parseAll(expression, "let a = b in c").get shouldBe ExpressionLet("a", "b", "c")
  }

  "let (a a') = b in c" should "be parsed" in {
    parseAll(expression, "let (a a') = b in c").successful shouldBe true
  }

  "let (a a') = b in c" should "be ExpressionLet" in {
    parseAll(expression, "let (a a') = b in c").get shouldBe ExpressionLet(PatternApplication("a", "a'"), "b", "c")
  }

}

