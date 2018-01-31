# Lambë v0

[![experimental](http://badges.github.io/stability-badges/dist/experimental.svg)](http://github.com/badges/stability-badges)
[![Build Status](https://travis-ci.org/d-plaindoux/lambe.svg?branch=master)](https://travis-ci.org/d-plaindoux/lambe?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/d-plaindoux/lambe/badge.svg?branch=master)](https://coveralls.io/github/d-plaindoux/lambe?branch=master)

This version is the first one used to bootstrap the whole system.

## Grammar

```
s0 ::=
    "module" moduleName import* entity*

import ::=
    "from" moduleName "import" "*"
    "from" moduleName "import" "(" identifier+ ")"

moduleName ::=
    identifier ("." identifier)*

entity ::=
    data
    trait
    define
    typeDef
    expressionDef

data ::=
   "data" generic* name ":" type

trait ::=
   "trait" name generic* ("with" type)* ("for" type)? ("{" (typeDef | data | trait)* "}")?

define ::=
   "define" generic* type ("for" type)? ("{" (expressionDef | data | trait | define)* "}")?

name ::=
    identifier
    "(" operator ")"

generic ::=
    "(" identifier (":" type)? ")"

type ::=
    "type"
    identifier
    "(" type ")"
    type type
    type -> type

typeDef ::=
    "def" generic* name ":" type

expressionDef ::=
    "def" selfPattern? name generic* pattern* "=" expression

selfPattern ::=
    "self" "(" pattern+ ")"

pattern ::=
    pattern "as" identifier
    INTEGER
    STRING
    identifier
    operator
    "(" pattern+ ")"

expression ::=
    INTEGER
    STRING
    "self"
    identifier ("from" moduleName)?
    operator ("from" moduleName)?
    "let" pattern "=" expression "in" expression
    expression expression
    pattern+ "->" expression
    "(" expression ")"
    "$" type
```
