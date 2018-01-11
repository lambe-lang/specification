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

  def traitDefinition: Parser[(List[ValueType], List[EntityAst])] =
    (definitionType ~ traitDefinition.?) ^^ {
      case definitionType ~ Some((definitionTypes, entityTypes)) => (List(definitionType) ++ definitionTypes, entityTypes)
      case definitionType ~ None => (List(definitionType), List())
    } | (dataExpression ~ traitDefinition.?) ^^ {
      case dataExpression ~ Some((definitionTypes, entityTypes)) => (definitionTypes, List(dataExpression) ++ entityTypes)
      case dataExpression ~ None => (List(), List(dataExpression))
    } | (traitExpression ~ traitDefinition.?) ^^ {
      case traitExpression ~ Some((definitionTypes, entityTypes)) => (definitionTypes, List(traitExpression) ++ entityTypes)
      case traitExpression ~ None => (List(), List(traitExpression))
    }

  def traitExpression: Parser[TraitEntity] =
    positioned((Tokens.$trait ~> name) ~ generic.* ~ ("{" ~> traitDefinition.? <~ "}").? ^^ {
      case name ~ generics ~ None => TraitEntity(name, generics, (List(),List()))
      case name ~ generics ~ Some(None) => TraitEntity(name, generics, (List(),List()))
      case name ~ generics ~ Some(Some(definitions)) => TraitEntity(name, generics, definitions)
    })

  def defineExpression: Parser[DefineEntity] =
    positioned((Tokens.$define ~> generic.*) ~ typeExpression ~ (Tokens.$for ~> typeExpression).? ~ ("{" ~> definitionExpression.+ <~ "}") ^^ {
      case generics ~ traitType ~ typeExpression ~ definitions => DefineEntity(generics, traitType, typeExpression.fold(traitType)(identity), definitions)
    })

  def entities: Parser[List[EntityAst]] =
    (dataExpression | traitExpression | defineExpression).*

}
