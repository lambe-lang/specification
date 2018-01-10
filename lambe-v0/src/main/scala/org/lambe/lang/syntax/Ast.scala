package org.lambe.lang.syntax

import scala.util.parsing.input.Positional

trait Coercions {
  implicit def toType(a: String): TypeAst = TypeIdentifier(a)

  implicit def toExpression(a: String): ExpressionAst = ExpressionIdentifier(a)

  implicit def toPattern(a: String): PatternAst = PatternIdentifier(a)
}

object TypeDef {
  type Generics = List[(String, TypeAst)]
  type Parameters = List[TypeAst]
}

// ---------------------------------------------------------------------------------------------------------------------

trait TypeAst extends Positional

case class TypeIdentifier(name: String) extends TypeAst

case class TypeApplication(left: TypeAst, right: TypeAst) extends TypeAst

case class TypeAbstraction(left: TypeAst, right: TypeAst) extends TypeAst

// ---------------------------------------------------------------------------------------------------------------------

trait PatternAst extends Positional

case class PatternIdentifier(name: String) extends PatternAst

case class PatternApplication(left: PatternAst, right: PatternAst) extends PatternAst

// ---------------------------------------------------------------------------------------------------------------------

trait ExpressionAst extends Positional

case class ExpressionIdentifier(name: String) extends ExpressionAst

case class ExpressionApplication(left: ExpressionAst, right: ExpressionAst) extends ExpressionAst

case class ExpressionAbstraction(left: PatternAst, right: ExpressionAst) extends ExpressionAst

case class ExpressionLet(identifier: String, value: ExpressionAst, body: ExpressionAst) extends ExpressionAst

// ---------------------------------------------------------------------------------------------------------------------

trait ValueAst extends Positional

case class ValueType(name: String, generics: TypeDef.Generics, spec: TypeAst) extends ValueAst

case class ValueExpression(name: String, selfPattern: Option[PatternAst], generics: TypeDef.Generics, spec: ExpressionAst) extends ValueAst

// ---------------------------------------------------------------------------------------------------------------------

trait EntityAst extends Positional

case class DataEntity(name: String, generics: TypeDef.Generics, spec: TypeAst) extends EntityAst

case class TraitEntity(name: String, generics: TypeDef.Generics, spec: List[ValueType]) extends EntityAst

case class DefineEntity(name: String, generics: TypeDef.Generics, data: TypeAst, spec: List[ValueExpression]) extends EntityAst