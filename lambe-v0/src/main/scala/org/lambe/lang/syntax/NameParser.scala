package org.lambe.lang.syntax

trait NameParser extends CoreParser {

  def name: Parser[String] = identifier | ("(" ~> operator <~ ")")

}
