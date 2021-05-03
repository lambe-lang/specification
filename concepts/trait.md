# Trait specification and implementation

**Kind** : Trait definition and expression

A trait is a conceptual model use for the structuration. This implies
a specification using the `trait` keyword.

## Grammar

```
trait ::= "trait" (id | op) t_param* for? with* ("{" entity* "}")?
impl  ::= "impl" type with* ("{" entity* "}")?
for   ::= "for" type
with  ::= "with" type
```

In fact the grammar reflect trait implementation and specification.
In reality a trait implementation is an expression, and a trait
specification is a type. Such specification can be declared as a type

For instance the trait specification:

```
trait Eval a b for a { sig eval : self -> b }
```

can also be specified with the following type definition:

```
type Eval = forall a b.trait for a { sig eval : self -> b } 
```

## Trait specification

**Kind**: Trait type

In a trait we can define:
- a comment
- a type
- a trait specification
- a trait implementation
- a function specification
- a function implementation
- a use

## Trait implementation

**Kind**: Trait Expression, Directive

A trait can be implemented and such implementation can be used at 
the expression level.