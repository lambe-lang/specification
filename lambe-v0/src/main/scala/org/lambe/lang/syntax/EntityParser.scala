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
    positioned((Tokens.$data ~> generic.*) ~ name ~ profileType ^^ {
      case generics ~ name ~ typeExpression => DataEntity(name, generics, typeExpression)
    })

  private def traitDefinitions: Parser[(List[ValueType], List[EntityAst])] =
    definitionType ~ traitDefinitions ^^ {
      case definition ~ ((definitionTypes, entityTypes)) => (List(definition) ++ definitionTypes, entityTypes)
    } | (dataExpression | traitExpression) ~ traitDefinitions ^^ {
      case expression ~ ((definitionTypes, entityTypes)) => (definitionTypes, List(expression) ++ entityTypes)
    } | success(List(), List())

  def traitExpression: Parser[TraitEntity] =
    positioned((Tokens.$trait ~> name) ~ generic.* ~ ("where" ~> "self" ~> ":" ~> typeExpression).? ~ ("{" ~> traitDefinitions <~ "}").? ^^ {
      case name ~ generics ~ self ~ None => TraitEntity(name, generics, self, (List(), List()))
      case name ~ generics ~ self ~ Some(definitions) => TraitEntity(name, generics, self, definitions)
    })

  private def defineDefinitions: Parser[(List[ValueExpression], List[EntityAst])] =
    definitionExpression ~ defineDefinitions ^^ {
      case definition ~ ((definitionExpressions, defineDefinitions)) => (List(definition) ++ definitionExpressions, defineDefinitions)
    } | (defineExpression | dataExpression | traitExpression) ~ defineDefinitions ^^ {
      case expression ~ ((definitionExpressions, defineDefinitions)) => (definitionExpressions, List(expression) ++ defineDefinitions)
    } | success(List(), List())

  def defineExpression: Parser[DefineEntity] =
    positioned((Tokens.$define ~> generic.*) ~ typeExpression ~ (Tokens.$for ~> typeExpression).? ~ ("{" ~> defineDefinitions <~ "}") ^^ {
      case generics ~ traitType ~ typeExpression ~ definitions => DefineEntity(generics, traitType, typeExpression.fold(traitType)(identity), definitions)
    })

  def entities: Parser[List[EntityAst]] =
    (dataExpression | traitExpression | defineExpression).*

}
