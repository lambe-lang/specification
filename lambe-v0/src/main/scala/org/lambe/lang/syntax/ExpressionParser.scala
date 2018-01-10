package org.lambe.lang.syntax

trait ExpressionParser extends PatternParser with CoreParser with Coercions {

  def letExpression: Parser[ExpressionAst] =
    positioned((Tokens.$let ~> identifier <~ "=") ~ expression ~ (Tokens.$in ~> expression) ^^ {
      case identifier ~ value ~ body => ExpressionLet(identifier, value, body)
    })

  def simpleExpression: Parser[ExpressionAst] =
    (Tokens.$self | operator | identifier) ^^ ExpressionIdentifier | ("(" ~> expression <~ ")") | letExpression | ("$" ~> expression)

  def appliedExpression: Parser[ExpressionAst] =
    positioned(simpleExpression ~ simpleExpression.* ^^ {
      case application ~ arguments => arguments.foldLeft(application)(ExpressionApplication)
    })

  def funExpression: Parser[ExpressionAst] =
    positioned(pattern.+ ~ ("->" ~> expression) ^^ {
      case parameters ~ expression => parameters.foldRight(expression)(ExpressionAbstraction)
    })

  def expression: Parser[ExpressionAst] =
    funExpression | appliedExpression
}
