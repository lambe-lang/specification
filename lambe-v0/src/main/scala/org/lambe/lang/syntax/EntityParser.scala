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

trait EntityParser extends TypeParser with ParameterParser with NameParser with DefinitionParser {

  def dataExpression: Parser[DataEntity] =
    positioned((Tokens.$data ~> name) ~ generic.* ~ profileType ^^ {
      case name ~ generics ~ typeExpression => DataEntity(name, generics, typeExpression)
    })

  def traitExpression: Parser[TraitEntity] =
    positioned((Tokens.$trait ~> name) ~ generic.* ~ opt("{" ~> definitionType.* <~ "}") ^^ {
      case name ~ generics ~ None => TraitEntity(name, generics, List())
      case name ~ generics ~ Some(specifications) => TraitEntity(name, generics, specifications)
    })

  def defineExpression: Parser[DefineEntity] =
    positioned((Tokens.$define ~> name) ~ generic.* ~ (Tokens.$for ~> typeExpression) ~ ("{" ~> definitionExpression.* <~ "}") ^^ {
      case name ~ generics ~ typeExpression ~ definitions => DefineEntity(name, generics, typeExpression, definitions)
    })

  def entities: Parser[List[EntityAst]] =
    (dataExpression | traitExpression | defineExpression).*

}

object Analyser {
  def main(args: Array[String]) = {
    // todo
  }
}