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

class DataParsersSpec extends FlatSpec with EntityParser with Matchers {
  // entity parsing

  private val value0 = "data Bool : type"

  value0 should "be parsed" in {
    parseAll(dataExpression, value0).successful shouldBe true
  }

  value0 should "be a DataEntity" in {
    parseAll(dataExpression, value0).get shouldBe DataEntity("Bool", List(), "type")
  }

  private val value1 = "data (a:type) List : type"

  value1 should "be parsed" in {
    parseAll(dataExpression, value1).successful shouldBe true
  }

  value1 should "be a DataEntity" in {
    parseAll(dataExpression, value1).get shouldBe DataEntity("List", List(("a", "type")),  "type")
  }

  private val value2 = "data (a:type) (::) : a -> List a -> List a"

  value2 should "be parsed" in {
    parseAll(dataExpression, value2).successful shouldBe true
  }

  value2 should "be a DataEntity" in {
    parseAll(dataExpression, value2).get shouldBe
      DataEntity("::", List(("a", "type")), TypeAbstraction("a",TypeAbstraction(TypeApplication("List", "a"),TypeApplication("List", "a"))))
  }

}
