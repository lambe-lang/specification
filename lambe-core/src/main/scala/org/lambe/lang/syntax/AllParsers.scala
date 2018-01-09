package org.lambe.lang.syntax

import scala.language.postfixOps
import scala.util.parsing.combinator._

trait CoreParser extends RegexParsers with Parsers {

  def numberLiteral: Parser[Int] = """[+-]?\d+""".r ^^ { s => s.toInt }

  def identifier: Parser[String] = """[a-zA-Z][0-9a-zA-Z_$]*'?""".r

  def operator: Parser[String] = """[#@&!_$*<>,;.:\/+=-]+""".r

  def unit: Parser[Unit] = "()" ^^ { _ => () }

}

trait TypeParser extends CoreParser {

  def simpleTypeExpression : Parser[TypeAst] =
    ("type" | identifier) ^^ TypeIdentifier | ("(" ~> typeExpression <~ ")")

  def appliedTypeExpression : Parser[TypeAst] =
    simpleTypeExpression ~ opt(appliedTypeExpression) ^^ { r => r._2 map { TypeApplication(r._1, _) } orElse{ Some(r._1) } get}

  def typeExpression : Parser[TypeAst] =
    appliedTypeExpression ~ opt("->" ~> typeExpression) ^^ { r => r._2 map { TypeAbstraction(r._1, _) } orElse{ Some(r._1) } get}

}

