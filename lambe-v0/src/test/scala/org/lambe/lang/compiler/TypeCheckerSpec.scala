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
    new TypeChecker(Gamma()).
      verify(ExpressionInteger(1)) shouldBe
      Success(TypeIdentifier("Int"))
  }

  "\"aa\"" should "be String" in {
    new TypeChecker(Gamma()).
      verify(ExpressionString("aa")) shouldBe
      Success(TypeIdentifier("String"))
  }

  "id" should "be in Gamma" in {
    new TypeChecker(Gamma(List(DefinitionEntity(ValueType("id", List(), TypeAbstraction("a", "a")))))).
      verify(ExpressionIdentifier("id", Option.empty)) shouldBe
      Success(TypeAbstraction("a", "a"))
  }

  "incr 1" should "be Int" in {
    new TypeChecker(Gamma(List(DefinitionEntity(ValueType("incr", List(), TypeAbstraction("Int", "Int")))))).
      verify(ExpressionApplication(ExpressionIdentifier("incr", Option.empty), ExpressionInteger(1))) shouldBe
      Success(TypeIdentifier("Int"))
  }

  "0 -> 1" should "be Int -> Int" in {
    new TypeChecker(Gamma()).
      verify(ExpressionAbstraction(PatternInteger(0), ExpressionInteger(1))) shouldBe
      Success(TypeAbstraction("Int", "Int"))
  }

}
