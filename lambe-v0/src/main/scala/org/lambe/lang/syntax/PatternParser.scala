package org.lambe.lang.syntax

trait PatternParser extends CoreParser with Coercions {

  def simplePattern: Parser[PatternAst] =
    positioned(identifier ^^ PatternIdentifier)

  def appliedPattern: Parser[PatternAst] =
    positioned("(" ~> simplePattern ~ pattern.* <~ ")" ^^ {
      case pattern ~ patterns => patterns.foldLeft(pattern)(PatternApplication)
    })

  def pattern: Parser[PatternAst] =
    simplePattern | appliedPattern

  def selfPattern: Parser[PatternAst] =
    positioned(Tokens.$self ~> appliedPattern)
}
