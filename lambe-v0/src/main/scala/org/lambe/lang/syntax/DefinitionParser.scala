package org.lambe.lang.syntax

trait DefinitionParser extends ExpressionParser with TypeParser with ParameterParser with NameParser {

  def definitionType: Parser[ValueType] =
    positioned((Tokens.$def ~> name) ~ generic.* ~ profileType ^^ {
      case name ~ generics ~ typeExpression => ValueType(name, generics, typeExpression)
    })

  def definitionExpression: Parser[ValueExpression] =
    positioned((Tokens.$def ~> selfPattern.?) ~ name ~ generic.* ~ pattern.* ~ ("=" ~> expression) ^^ {
      case selfPattern ~ name ~ generics ~ parameters ~ expression => ValueExpression(name, selfPattern, generics, parameters.foldRight(expression)(ExpressionAbstraction))
    })

}
