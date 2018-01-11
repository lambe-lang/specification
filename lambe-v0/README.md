# Lambë v0

This version is the first one used to bootstrap the whole system.

## Grammar

```
s0 ::=
   "data"  name generic* type? -> type
   "trait" name generic* ("{" typeDef* "}")?
   "define" generic* type for type ("{" expressionDef* "}")?

name ::=
    identifier
    "(" operator ")"

generic ::=
    "[" identifier ":" type "]"

type ::=
    "type"
    identifier
    "(" type ")"
    "$" type
    type type
    type -> type

typeDef ::=
    "def" name generic* type? "->" type

expressionDef ::=
    "def" selfPattern? name generic* pattern* "=" expression

selfPattern ::=
    "self" pattern

pattern ::=
    identifier
    "(" pattern+ ")"

expression ::=
    "self"
    identifier
    "let" pattern "=" expression "in" expression
    expression expression
    pattern+ "->" expression
    "(" expression ")"
```