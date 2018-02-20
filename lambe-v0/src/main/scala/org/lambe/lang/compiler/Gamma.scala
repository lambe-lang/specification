package org.lambe.lang.compiler

import org.lambe.lang.syntax.TypeDef.Generics
import org.lambe.lang.syntax._


class Gamma(current: List[EntityAst], imports: List[ModuleAst]) {

  def findType(name: String): Option[(Generics, TypeAst)] =
    findData(name) orElse findFunction(name)

  def findData(name: String): Option[(Generics, TypeAst)] =
    current flatMap {
      case d : DataEntity => List(d)
      case _ => List()
    } find { d => d.name == name
    } map { d => (d.generics, d.spec) }

  def findFunction(name: String): Option[(Generics, TypeAst)] =
    current flatMap {
      case DefinitionEntity(d: ValueType) => List(d)
      case _ => List()
    } find { v => v.name == name
    } map { d => (d.generics, d.spec) }

}

object Gamma {
  def apply(): Gamma = new Gamma(List(), List())

  def apply(current: List[EntityAst]): Gamma = new Gamma(current, List())

  def apply(current: List[EntityAst], imports: List[ModuleAst]): Gamma = new Gamma(current, imports)
}
