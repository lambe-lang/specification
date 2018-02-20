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

import scala.util.parsing.input.Position
import scala.util.{Failure, Success, Try}

trait TypeException extends Exception

case class NotAnAbstraction(pos: Position) extends TypeException

case class NotDefined(pos: Position) extends TypeException

class TypeChecker(val gamma: Gamma) {

  def verify(e: PatternAst): Try[TypeAst] = e match {
    case PatternInteger(_) =>
      Success(TypeIdentifier("Int"))

    case PatternString(_) =>
      Success(TypeIdentifier("String"))

    case PatternApplication(left, right) =>
      for (tright <- verify(right);
           tleft <- verify(left);
           tresult <- tleft match {
             case TypeAbstraction(fleft, fright) if fleft == tright => Success(fright)
             case _ => Failure(NotAnAbstraction(e.pos))
           })
        yield tresult

    case PatternIdentifier(name) =>
      gamma.findData(name).map {
        _._2
      }.fold[Try[TypeAst]] {
        Failure(NotDefined(e.pos)) // TODO
      } {
        Success(_)
      }

    case PatternAlias(p, _) => verify(p)
  }

  def verify(e: ExpressionAst): Try[TypeAst] = e match {
    case ExpressionInteger(_) =>
      Success(TypeIdentifier("Int"))

    case ExpressionString(value) =>
      Success(TypeIdentifier("String"))

    case ExpressionApplication(left, right) =>
      for (tright <- verify(right);
           tleft <- verify(left);
           tresult <- tleft match {
             case TypeAbstraction(fleft, fright) if fleft == tright => Success(fright)
             case _ => Failure(NotAnAbstraction(e.pos))
           })
        yield tresult

    case ExpressionIdentifier(name, _) => // TODO
      gamma.findType(name).map {
        _._2
      }.fold[Try[TypeAst]] {
        Failure(NotDefined(e.pos))
      } {
        Success(_)
      }

    case ExpressionAbstraction(left, right) =>
      for (tright <- verify(right);
           tleft <- verify(left))
        yield TypeAbstraction(tleft, tright)

    case ExpressionLet(binding, value, body) => Failure(???)
  }

}
