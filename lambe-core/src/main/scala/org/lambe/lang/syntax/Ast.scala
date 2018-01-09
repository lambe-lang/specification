package org.lambe.lang.syntax

trait TypeAst
case class TypeIdentifier(name: String) extends TypeAst
case class TypeApplication(left: TypeAst, right: TypeAst) extends TypeAst
case class TypeAbstraction(left: TypeAst, right: TypeAst) extends TypeAst
