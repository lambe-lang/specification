package org.lambe.lang.syntax

trait ParameterParser extends TypeParser {

  def generic: Parser[(String, TypeAst)] =
    ("[" ~> identifier <~ ":") ~ (positioned(typeExpression) <~ "]") ^^ {
      case identifier ~ typeExpression => (identifier, typeExpression)
    }

  def parameter: Parser[TypeAst] =
    positioned(simpleTypeExpression)

  def profileType: Parser[TypeAst] =
    positioned(funTypeExpression | ("->" ~> appliedTypeExpression))
}
