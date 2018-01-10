package org.lambe.lang.syntax

object Tokens {
  val $trait = "trait"
  val $data = "data"
  val $define = "define"
  val $def = "def"
  val $let = "let"
  val $in = "in"
  val $for = "for"
  val $type = "type"
  val $self = "self"

  val keywords = List($trait, $data, $define, $def, $let, $in, $for, $type, $self)
  val separators = List("[", "]", "(", ")", "->", "=", "$")
}
