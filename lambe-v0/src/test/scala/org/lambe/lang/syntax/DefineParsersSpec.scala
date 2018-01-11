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

  "define Boolean for Bool { def self(true) (||) = false def self(false) (||) = self }" should "be parsed" in {
    parseAll(defineExpression, "define Boolean for Bool { def self(true) (||) = false def self(false) (||) = self }").successful shouldBe true
  }

  "define Boolean for Bool { def self(true) (||) _ = self def self(false) (||) a = a }" should "be DefineEntity" in {
    parseAll(defineExpression, "define Boolean for Bool { def self(true) (||) _ = self def self(false) (||) a = a }").get shouldBe
      DefineEntity(
        "Boolean",
        List(),
        "Bool",
        List(
          ValueExpression(
            "||",
            Option("true"),
            List(),
            ExpressionAbstraction("_","self")
          ),
          ValueExpression(
            "||",
            Option("false"),
            List(),
            ExpressionAbstraction("a","a")
          )
        )
      )
  }

}
