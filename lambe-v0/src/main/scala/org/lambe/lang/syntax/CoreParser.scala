package org.lambe.lang.syntax

import scala.util.parsing.combinator.{Parsers, RegexParsers}

trait CoreParser extends RegexParsers with Parsers {

  def numberLiteral: Parser[Int] = """[+-]?\d+""".r ^^ { s => s.toInt }

  def identifier: Parser[String] =
    """[_a-zA-Z][_0-9a-zA-Z_$]*'?""".r ^? {
      case m if !Tokens.keywords.contains(m) => m
    }

  def operator: Parser[String] =
    """[#@&!_$*<>,;.:\/+=|-]+""".r ^? {
      case m if !Tokens.separators.contains(m) => m
    }

  def unit: Parser[Unit] = "()" ^^ { _ => () }

}
