package org.lambe.lang.syntax

import scala.language.postfixOps
import scala.util.parsing.combinator._

object Tokens {
  val keywords = List("trait", "val", "data")
  val separators = List("[", "]", "(", ")", "->", ":")
}

trait CoreParser extends RegexParsers with Parsers {

  def numberLiteral: Parser[Int] = """[+-]?\d+""".r ^^ { s => s.toInt }

  def identifier: Parser[String] =
    """[a-zA-Z][0-9a-zA-Z_$]*'?""".r ^? {
      case m if !Tokens.keywords.contains(m) => m
    }

  def operator: Parser[String] =
    """[#@&!_$*<>,;.:\/+=|-]+""".r ^? {
      case m if !Tokens.separators.contains(m) => m
    }

  def unit: Parser[Unit] = "()" ^^ { _ => () }

}

trait NameParser extends CoreParser {

  def name: Parser[String] = identifier | ("(" ~> operator <~ ")")

}

trait TypeParser extends CoreParser with Coercions {

  def simpleTypeExpression: Parser[TypeAst] =
    (identifier ^^ TypeIdentifier) | ("(" ~> typeExpression <~ ")")

  def appliedTypeExpression: Parser[TypeAst] =
    simpleTypeExpression ~ opt(appliedTypeExpression) ^^ {
      case leftTypeExpression ~ None => leftTypeExpression
      case leftTypeExpression ~ Some(rightTypeExpression) => TypeApplication(leftTypeExpression, rightTypeExpression)
    }

  def funTypeExpression: Parser[TypeAst] =
    appliedTypeExpression ~ ("->" ~> typeExpression) ^^ {
      case leftTypeExpression ~ rightTypeExpression => TypeAbstraction(leftTypeExpression, rightTypeExpression)
    }

  def typeExpression: Parser[TypeAst] =
    funTypeExpression | appliedTypeExpression
}

trait ParameterParser extends TypeParser {

  def generics: Parser[(String, TypeAst)] =
    ("[" ~> identifier <~ ":") ~ (typeExpression <~ "]") ^^ {
      case identifier ~ typeExpression => (identifier, typeExpression)
    }

  def parameter: Parser[TypeAst] =
    simpleTypeExpression

  def profileType: Parser[TypeAst] =
    funTypeExpression | ("->" ~> appliedTypeExpression)
}

trait BehaviorParser extends TypeParser with ParameterParser with NameParser {

  def valueType: Parser[ValueType] =
    ("val" ~> name) ~ rep(generics) ~ profileType ^^ {
      case name ~ generics ~ typeExpression => ValueType(name, generics, typeExpression)
    }

  /*
    def valueDefinition: Parser[FunctionAst] =
      ("def" ~> name) ~ rep(generics) ~ rep(parameter) ~ ("=" ~> behaviorExpression) ^^ {
        case name ~ generics ~ parameters ~ typeExpression => FunctionAst(name, generics, parameters, typeExpression)
      }
  */
}

trait EntityParser extends TypeParser with ParameterParser with NameParser with BehaviorParser {

  def dataExpression: Parser[DataEntity] =
    ("data" ~> name) ~ rep(generics) ~ profileType ^^ {
      case name ~ generics ~ typeExpression => DataEntity(name, generics, typeExpression)
    }

  def traitExpression: Parser[TraitEntity] =
    ("trait" ~> name) ~ rep(generics) ~ opt("{" ~> rep(valueType) <~ "}") ^^ {
      case name ~ generics ~ None => TraitEntity(name, generics, List())
      case name ~ generics ~ Some(specifications) => TraitEntity(name, generics, specifications)
    }

}
