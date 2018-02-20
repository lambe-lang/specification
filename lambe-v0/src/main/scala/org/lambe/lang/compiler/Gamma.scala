package org.lambe.lang.compiler

import org.lambe.lang.syntax.TypeDef.Generics
import org.lambe.lang.syntax._


class Gamma(current: List[EntityAst], imports: List[ModuleAst]) {

  def findType(name: String): Option[(Generics,TypeAst)] =
    findData(name) orElse findFunction(name)

  def findData(name: String): Option[(Generics,TypeAst)] =
    current find {
      case DataEntity(n, _, _) => n == name
      case _ => false
    } flatMap {
      case d: DataEntity => Some(d.generics,d.spec)
      case _ => None
    }

  def findFunction(name: String): Option[(Generics,TypeAst)] =
    current find {
      case DefinitionEntity(ValueType(n, _, _)) => n == name
      case _ => false
    } flatMap {
      case DefinitionEntity(d: ValueType) => Some(d.generics,d.spec)
      case _ => None
    }

}

object Gamma {
  def apply(): Gamma = new Gamma(List(), List())

  def apply(current: List[EntityAst]): Gamma = new Gamma(current, List())

  def apply(current: List[EntityAst], imports: List[ModuleAst]): Gamma = new Gamma(current, imports)
}
