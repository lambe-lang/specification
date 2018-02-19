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

package org.lambe.lang.compiler

import org.lambe.lang.syntax._
import org.scalatest._

import scala.util.Success

class TypeCheckerSpec extends FlatSpec with Matchers with Coercions {

  "1" should "be Int" in {
    new TypeChecker(Map()).
      verify(ExpressionInteger(1)) shouldBe
      Success(TypeIdentifier("Int"))
  }

  "\"aa\"" should "be String" in {
    new TypeChecker(Map()).
      verify(ExpressionString("aa")) shouldBe
      Success(TypeIdentifier("String"))
  }

  "id" should "be in Gamma" in {
    new TypeChecker(Map(("id", TypeForall("a", "type", TypeAbstraction("a", "a"))))).
      verify(ExpressionIdentifier("id", Option.empty)) shouldBe
      Success(TypeForall("a", "type", TypeAbstraction("a", "a")))
  }

  "incr 1" should "be Int" in {
    new TypeChecker(Map(("incr", TypeAbstraction("Int", "Int")))).
      verify(ExpressionApplication(ExpressionIdentifier("incr", Option.empty), ExpressionInteger(1))) shouldBe
      Success(TypeIdentifier("Int"))
  }

}
