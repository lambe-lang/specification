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

import scala.util.parsing.combinator.{Parsers, RegexParsers}

trait CoreParser extends RegexParsers with Parsers {

  def numberLiteral: Parser[Int] = """[+-]?\d+""".r ^^ { s => s.toInt }

  def identifier: Parser[String] =
    """[_a-zA-Z][_0-9a-zA-Z_$]*'?""".r ^? {
      case m if !Tokens.keywords.contains(m) => m
    }

  def operator: Parser[String] =
    """[#@&!_$*<>,;.:\/+=|-]+""".r ^? {
      case m if !Tokens.separators.contains(m) => m
    }

  def unit: Parser[Unit] = "()" ^^ { _ => () }

}
