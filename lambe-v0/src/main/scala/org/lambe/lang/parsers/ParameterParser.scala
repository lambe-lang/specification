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

trait ParameterParser extends TypeParser {

  private def generic: Parser[(String, TypeAst)] =
    identifier ~ (":" ~> positioned(typeExpression)).? ^^ {
      case identifier ~ None => (identifier, "type")
      case identifier ~ Some(typeExpression) => (identifier, typeExpression)
    }

  def typeParameters : Parser[List[(String, TypeAst)]] =
    ("(" ~> generic <~ ")").*

  def forallParameters: Parser[List[(String, TypeAst)]] =
    ("[" ~> generic ~ ("," ~> generic).* <~ "]").? ^^ {
      case None => List()
      case Some(h ~ t) => h :: t
    }

}
