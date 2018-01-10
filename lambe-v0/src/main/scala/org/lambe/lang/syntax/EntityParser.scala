package org.lambe.lang.syntax

trait EntityParser extends TypeParser with ParameterParser with NameParser with DefinitionParser {

  def dataExpression: Parser[DataEntity] =
    positioned((Tokens.$data ~> name) ~ generic.* ~ profileType ^^ {
      case name ~ generics ~ typeExpression => DataEntity(name, generics, typeExpression)
    })

  def traitExpression: Parser[TraitEntity] =
    positioned((Tokens.$trait ~> name) ~ generic.* ~ opt("{" ~> definitionType.* <~ "}") ^^ {
      case name ~ generics ~ None => TraitEntity(name, generics, List())
      case name ~ generics ~ Some(specifications) => TraitEntity(name, generics, specifications)
    })

  def defineExpression: Parser[DefineEntity] =
    positioned((Tokens.$define ~> name) ~ generic.* ~ (Tokens.$for ~> typeExpression) ~ ("{" ~> definitionExpression.* <~ "}") ^^ {
      case name ~ generics ~ typeExpression ~ definitions => DefineEntity(name, generics, typeExpression, definitions)
    })

  def entities: Parser[List[EntityAst]] =
    (dataExpression | traitExpression | defineExpression).*

}
