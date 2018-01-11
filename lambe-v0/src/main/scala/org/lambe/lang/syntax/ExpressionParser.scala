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

trait ExpressionParser extends PatternParser with CoreParser with Coercions {

  def letExpression: Parser[ExpressionAst] =
    positioned((Tokens.$let ~> identifier <~ "=") ~ expression ~ (Tokens.$in ~> expression) ^^ {
      case identifier ~ value ~ body => ExpressionLet(identifier, value, body)
    })

  def simpleExpression: Parser[ExpressionAst] =
    positioned((Tokens.$self | operator | identifier) ^^ ExpressionIdentifier | ("(" ~> expression <~ ")") | letExpression | ("$" ~> expression))

  def appliedExpression: Parser[ExpressionAst] =
    positioned(simpleExpression ~ simpleExpression.* ^^ {
      case application ~ arguments => arguments.foldLeft(application)(ExpressionApplication)
    })

  def funExpression: Parser[ExpressionAst] =
    positioned(pattern.+ ~ ("->" ~> expression) ^^ {
      case parameters ~ expression => parameters.foldRight(expression)(ExpressionAbstraction)
    })

  def expression: Parser[ExpressionAst] =
    funExpression | appliedExpression
}
