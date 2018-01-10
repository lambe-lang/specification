package org.lambe.lang.syntax

trait TypeParser extends CoreParser with Coercions {

  def simpleTypeExpression: Parser[TypeAst] =
    positioned((Tokens.$type | identifier) ^^ TypeIdentifier | ("(" ~> typeExpression <~ ")"))

  def appliedTypeExpression: Parser[TypeAst] =
    positioned(simpleTypeExpression ~ appliedTypeExpression.? ^^ {
      case leftTypeExpression ~ None => leftTypeExpression
      case leftTypeExpression ~ Some(rightTypeExpression) => TypeApplication(leftTypeExpression, rightTypeExpression)
    })

  def funTypeExpression: Parser[TypeAst] =
    positioned(appliedTypeExpression ~ ("->" ~> typeExpression) ^^ {
      case leftTypeExpression ~ rightTypeExpression => TypeAbstraction(leftTypeExpression, rightTypeExpression)
    })

  def typeExpression: Parser[TypeAst] =
    funTypeExpression | appliedTypeExpression
}
