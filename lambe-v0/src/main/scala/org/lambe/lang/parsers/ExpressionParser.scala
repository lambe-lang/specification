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

trait ExpressionParser extends PatternParser with TokenParser with NameParser {

  def letExpression: Parser[ExpressionAst] =
    positioned((Tokens.$let ~> pattern <~ "=") ~ expression ~ (Tokens.$in ~> expression) ^^ {
      case binding ~ value ~ body => ExpressionLet(binding, value, body)
    })

  def simpleExpression: Parser[ExpressionAst] =
    positioned(
      integerLiteral ^^ ExpressionInteger
        | stringLiteral ^^ ExpressionString
        | expressionOperator ~ (Tokens.$from ~> moduleName).? ^^ { case o ~ m => ExpressionOperator(o, m) }
        | ("(" ~> expressionOperator <~ ")" | identifier) ~ (Tokens.$from ~> moduleName).? ^^ { case o ~ m => ExpressionIdentifier(o, m) }
        | ("(" ~> expression <~ ")")
        | letExpression
        | ("$" ~> expression)
    )

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
