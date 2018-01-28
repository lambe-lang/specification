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

import org.lambe.lang.syntax.ModuleAst
import org.scalatest._

class ModuleParsersSpec extends FlatSpec with ModuleParser with Matchers {

  private val value0 = "module T"

  value0 should "be parsed" in {
    parseAll(module, value0).successful shouldBe true
  }

  value0 should "be ModuleAst" in {
    parseAll(module, value0).get shouldBe ModuleAst("T", List(), List())
  }

  private val value1 = "module T from A import *"

  value1 should "be parsed" in {
    parseAll(module, value1).successful shouldBe true
  }

  value1 should "be ModuleAst" in {
    parseAll(module, value1).get shouldBe ModuleAst("T", List(("A", List())), List())
  }

  private val value2 = "module T from A import (a b)"

  value2 should "be parsed" in {
    parseAll(module, value2).successful shouldBe true
  }

  value2 should "be ModuleAst" in {
    parseAll(module, value2).get shouldBe ModuleAst("T", List(("A", List("a", "b"))), List())
  }

  private val value3 = "module T from A import (a b) from B import *"

  value3 should "be parsed" in {
    parseAll(module, value3).successful shouldBe true
  }

  value3 should "be ModuleAst" in {
    parseAll(module, value3).get shouldBe ModuleAst("T", List(("A", List("a", "b")), ("B", List())), List())
  }
}
