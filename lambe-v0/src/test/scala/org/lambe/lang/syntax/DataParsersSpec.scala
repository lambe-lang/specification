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

class DataParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  "data Bool -> type" should "be parsed" in {
    parseAll(dataExpression, "data Bool -> type").successful shouldBe true
  }

  "data Bool -> type" should "be a DataEntity" in {
    parseAll(dataExpression, "data Bool -> type").get shouldBe DataEntity("Bool", List(), "type")
  }

  "data List [a:type] -> type" should "be parsed" in {
    parseAll(dataExpression, "data List[a:type] -> type").successful shouldBe true
  }

  "data List [a:type] -> type" should "be a DataEntity" in {
    parseAll(dataExpression, "data List[a:type] -> type").get shouldBe DataEntity("List", List(("a", "type")),  "type")
  }

  "data (::) [a:type] a -> List a -> type" should "be parsed" in {
    parseAll(dataExpression, "data (::)[a:type] a -> List a -> type").successful shouldBe true
  }

  "data (::) [a:type] a -> List a ->  List a" should "be a DataEntity" in {
    parseAll(dataExpression, "data (::)[a:type] a -> List a -> List a").get shouldBe
      DataEntity("::", List(("a", "type")), TypeAbstraction("a",TypeAbstraction(TypeApplication("List", "a"),TypeApplication("List", "a"))))
  }

}
