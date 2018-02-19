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

import scala.util.{Failure, Success, Try}

class TypeChecker(val gamma: Map[String, TypeAst]) {

  def verify(e: ExpressionAst): Try[TypeAst] = {
    e match {
      case ExpressionInteger(_) =>
        Success(TypeIdentifier("Int"))

      case ExpressionString(value) =>
        Success(TypeIdentifier("String"))

      case ExpressionApplication(left, right) =>
        verify(right).flatMap(tright => {
          verify(left).flatMap {
            case TypeAbstraction(fleft, fright) if fleft == tright => Success(fleft)
            case _ => Failure(???)
          }
        })

    case ExpressionIdentifier(name, _) =>
      gamma.get(name).fold[Try[TypeAst]] {
        Failure(???)
      } {
        Success(_)
      }

    case ExpressionAbstraction(left, right) => Failure(???)
    case ExpressionLet(binding, value, body) => Failure(???)
  }
}

}
