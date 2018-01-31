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

trait ModuleParser extends EntityParser with TypeParser with ParameterParser with NameParser with DefinitionParser {

  def imports: Parser[(ModuleNameAst, List[String])] =
    (Tokens.$from ~> moduleName <~ Tokens.$import) <~ "*" ^^ {
      case name => (ModuleNameAst(name), List())
    } | (Tokens.$from ~> moduleName <~ Tokens.$import) ~ ("(" ~> identifier.+ <~ ")")  ^^ {
      case name ~ imports => (ModuleNameAst(name), imports)
    }

  def module: Parser[ModuleAst] =
    (Tokens.$module ~> moduleName) ~ imports.* ~ entities ^^ {
      case name ~ imports ~ exports => ModuleAst(ModuleNameAst(name), imports, exports)
    }

}
