package org.lambe.lang.syntax

object TypeDef {
  type Generics = List[(String, TypeAst)]
  type Parameters = List[TypeAst]
}

trait Coercions {
  implicit def toType(a: String): TypeAst = TypeIdentifier(a)
}

trait TypeAst

case class TypeIdentifier(name: String) extends TypeAst

case class TypeApplication(left: TypeAst, right: TypeAst) extends TypeAst

case class TypeAbstraction(left: TypeAst, right: TypeAst) extends TypeAst

trait ValueAst

case class ValueType(name: String, generics: TypeDef.Generics, spec: TypeAst) extends ValueAst

trait EntityAst

case class DataEntity(name: String, generics: TypeDef.Generics, spec: TypeAst) extends EntityAst

case class TraitEntity(name: String, generics: TypeDef.Generics, spec: List[ValueType]) extends EntityAst