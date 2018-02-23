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

trait EntityParser extends TypeParser with ParameterParser with NameParser with DefinitionParser {

  def dataExpression: Parser[DataEntity] =
    positioned((Tokens.$data ~>  name <~ ":") ~ forallParameters ~ typeExpression ^^ {
      case name ~ generics ~ typeExpression => DataEntity(name, generics, typeExpression)
    })

  private def traitDefinitions: Parser[(List[ValueType], List[EntityAst])] =
    definitionType ~ traitDefinitions ^^ {
      case definition ~ ((definitionTypes, entityTypes)) => (List(definition) ++ definitionTypes, entityTypes)
    } | (dataExpression | traitExpression) ~ traitDefinitions ^^ {
      case expression ~ ((definitionTypes, entityTypes)) => (definitionTypes, List(expression) ++ entityTypes)
    } | success(List(), List())

  def traitExpression: Parser[TraitEntity] =
    positioned((Tokens.$trait ~> name) ~ typeParameters ~ (Tokens.$with ~> typeExpression).* ~ ("{" ~> traitDefinitions <~ "}").? ^^ {
      case name ~ generics ~ extensions ~ None => TraitEntity(name, generics, extensions, (List(), List()))
      case name ~ generics ~ extensions ~ Some(definitions) => TraitEntity(name, generics, extensions, definitions)
    })

  private def defineDefinitions: Parser[(List[ValueExpression], List[EntityAst])] =
    definitionExpression ~ defineDefinitions ^^ {
      case definition ~ ((definitionExpressions, defineDefinitions)) => (List(definition) ++ definitionExpressions, defineDefinitions)
    } | (defineExpression | dataExpression | traitExpression) ~ defineDefinitions ^^ {
      case expression ~ ((definitionExpressions, defineDefinitions)) => (definitionExpressions, List(expression) ++ defineDefinitions)
    } | success(List(), List())

  def defineExpression: Parser[DefineEntity] =
    positioned((Tokens.$define ~> forallParameters) ~ typeExpression ~ (Tokens.$with ~> typeExpression).* ~ ("{" ~> defineDefinitions <~ "}") ^^ {
      case generics ~ traitType ~ extensions ~ definitions => DefineEntity(generics, traitType, extensions, definitions)
    })

  def definition: Parser[EntityAst] =
    (definitionExpression | definitionType).map {
      DefinitionEntity
    }

  def entities: Parser[List[EntityAst]] =
    (dataExpression | traitExpression | defineExpression | definition).*

}
