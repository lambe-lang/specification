# Lambë v0

[![experimental](http://badges.github.io/stability-badges/dist/experimental.svg)](http://github.com/badges/stability-badges)
[![Build Status](https://travis-ci.org/d-plaindoux/lambe.svg?branch=master)](https://travis-ci.org/d-plaindoux/lambe?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/d-plaindoux/lambe/badge.svg?branch=master)](https://coveralls.io/github/d-plaindoux/lambe?branch=master)

This version is the first one used to bootstrap the whole system.

## Some Examples

### Function composition

```
def (a) (b) (c) compose : (a -> b) -> (b -> c) -> a -> c
def compose f g = x -> g $ f x
```

### Trampoline definition

```
data Trampoline : type -> type
data (a) Done : a -> Trampoline a
data (a) Next : (Unit -> Trampoline a) -> Trampoline a
```

### Runnable definition

```
trait Runnable (a) {
    def run : a
}
```

### Runnable Trampoline implementation

```
define (a) Runnable a for Trampoline a {
    def self(Done a) run = a
    def self(Next f) run = f unit run
}
```

## Grammar

```
s0 ::=
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
    "self" pattern

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
    identifier
    "let" pattern "=" expression "in" expression
    expression expression
    pattern+ "->" expression
    "(" expression ")"
    "$" type
```
