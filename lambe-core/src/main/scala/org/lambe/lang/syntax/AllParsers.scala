package org.lambe.lang.syntax

import scala.language.postfixOps
import scala.util.parsing.combinator._

trait CoreParser extends RegexParsers with Parsers {

  def numberLiteral: Parser[Int] = """[+-]?\d+""".r ^^ { s => s.toInt }

  def identifier: Parser[String] = """[a-zA-Z][0-9a-zA-Z_$]*'?""".r

  def operator: Parser[String] = """[#@&!_$*<>,;.:\/+=-]+""".r

  def unit: Parser[Unit] = "()" ^^ { _ => () }

}

trait KeyParser extends RegexParsers with Parsers {

  def KWtype: Parser[String] = "type"

  def KWdata: Parser[String] = "data"

}

trait NameParser extends CoreParser {

  def name: Parser[String] = identifier | ("(" ~> operator <~ ")")

}

trait TypeParser extends CoreParser with KeyParser {

  def simpleTypeExpression: Parser[TypeAst] =
    (KWtype | identifier) ^^ TypeIdentifier | ("(" ~> typeExpression <~ ")")

  def appliedTypeExpression: Parser[TypeAst] =
    simpleTypeExpression ~ opt(appliedTypeExpression) ^^ {
      case leftTypeExpression ~ None => leftTypeExpression
      case leftTypeExpression ~ Some(rightTypeExpression) => TypeApplication(leftTypeExpression, rightTypeExpression)
    }

  def typeExpression: Parser[TypeAst] =
    appliedTypeExpression ~ opt("->" ~> typeExpression) ^^ {
      case leftTypeExpression ~ None => leftTypeExpression
      case leftTypeExpression ~ Some(rightTypeExpression) => TypeAbstraction(leftTypeExpression, rightTypeExpression)
    }
}

trait ParameterParser extends TypeParser {

  def generics: Parser[(String, TypeAst)] =
    ("[" ~> identifier <~ ":") ~ (typeExpression <~ "]") ^^ {
      case identifier ~ typeExpression => (identifier, typeExpression)
    }

  def parameter: Parser[TypeAst] =
    "(" ~> typeExpression <~ ")"
}

trait EntityParser extends TypeParser with ParameterParser with  NameParser {

  def dataExpression: Parser[DataEntity] =
    (KWdata ~> name) ~ rep(generics) ~ rep(parameter) ~ (":" ~> typeExpression) ^^ {
      case name ~ generics ~ parameters ~ typeExpression => DataEntity(name, generics, parameters, typeExpression)
    }

}
