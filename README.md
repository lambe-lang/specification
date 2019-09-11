# Lambë

A statically typed functional programming language inspired by Haskell, OCaml and Rust.

## 0. Paradigms

Targeted programming language paradigms for the design of Lambë are:
- [X] Functional programming,
- [X] Static typing,
- [X] Self receiver concept,
- [X] Algebraic Data Type aka ADT.  
- [X] Trait based code organisation,
- [X] Coarse and fine grain self specification i.e. receiver type,
- [X] Trait implementation as first class citizen,
- [X] Higher-kinded-type,
- [X] Smart cast

## 1. Function

**Keyword**: Functional programming, Static typing

### 1.1 Basic concept

#### Definition

```
sig id       : a -> a
sig swap     : (a -> b -> c) -> b -> a -> c
sig compose  : (b -> c) -> (a -> b) -> a -> c
sig pipeline : (a -> b) -> (b -> c) -> a -> c
```

#### Implementation

```
def id       = { a -> a }         // equivalent to { $1 }
def swap     = { f x y -> f y x } // equivalent to { $1 $3 $2 }
def compose  = { $1 ($2 $3) }     // equivalent to { f g x -> f (g x) }
def pipeline = swap compose
 ```

### 1.2 Self concept

**Keyword**: Self receiver concept

A function can be specified with a `self` type at the first position. Therefor such function is infix and accept the
dot notation. The self type is given by the attached `for` directive.

#### Definition

```
sig ($)  : self -> (a -> b) -> a -> c for b -> c
sig (|>) : self -> (b -> c) -> a -> c for a -> b
```

#### Implementation

```
def ($) f a = self (f a)
def (|>) f = f $ self
 ```

#### Function with self definition in Action

```
// for FP addicts
1 + $ 3 + 4
3 + |> 4 + |> 2 * 5 

// for OO with FP flavor addicts
1.+.$ 3.+ 4
3.+.|> 4.+.|> 2.* 5 
```
 
## 2. Data type

**Keyword**: Algebraic Data Type

### 2.1 Data type definition

```
data None
data Some a { v:a }
type Option a = None | Some a
```

Each data has a corresponding constructor denoted by a function when parameters are defined thanks to attributes specification based order:

```
sig None : None
sig Some : a  -> Some a
```

In addition data definition can be done at the type level:

```
type Option a = 
  data None 
| data Some { v:a }
```

Synthesised type variables is done using the original order given a the type level definition.

### 2.2 Data type implementation

```
impl for Option a {
    sig fold: self -> (None -> b) -> (Some a -> b) -> b

    def None.fold n _ = n self // self : None
    def Some.fold _ s = s self // self : Some a
}
```

In this implementation for `Option a` we use a type named `self`. In fact self denotes the type of the receiver which is `Option a` in this case defined thanks to the `for ...` declaration. Furthermore implementations are
given for each option data type i.e. None and Some.

### 2.3 Data type in action

```
// for FP addicts
Some 1 fold { 0 } id

// for OO with FP flavor addicts
(Some 1).fold { 0 } id
```

**Keyword**: Smart Cast

Lambë does not provide a pattern matching but a Kotlin like smart cast on types.

```
impl for Option a {
    sig fold: self -> (None -> b) -> (Some a -> b) -> b

    def fold n s = 
        when self {
          is None -> n self
          is Some -> s self
        }
}
``` 

## 4. Traits

**Keyword**: Trait based code organisation

### Trait definition

```
trait Functor (f:type->type) {
    sig fmap : self -> (a -> b) -> f b for f a
    sig <$>  : self -> f a -> f b for a -> b
    
    def <$> a = a fmap f
}
```

The `Functor` has a parametric type constructor `f` revealing the support of higher-kinded-types in the language.

The `fmap` has a receiver called `self` and this receiver has the following type given by the *for* directive: `f a`.

```
trait Applicative (f:type->type) with Functor f {
    sig pure   : a -> f a
    sig (<*>)  : self -> f a -> f b for f (a -> b)
    sig (<**>) : self -> f (a -> b) -> f b for f a

    def (<**>) a = a <*> self
}
```

Such *for* directive can be define at the trait level, signature level or definition level. If such directive is not expressed for a method and does not have `self` as first parameter it's a *static* method. 

```
trait Monad (f:type->type) with Applicative f {
    sig return : a -> f a 
    sig join   : self -> f a for f (f a)
    sig (>>=)  : self -> (a -> f b) -> f b for f a
    sig (=<<)  : self -> f a -> f b for a -> f b

    def return  = pure
    def (>>=) f = self fmap f join
    def (=<<) a = a >>= self
}
```

Finally each method can be specified with a dedicated `self` type. As a conclusion a trait define a logical development unit.

### Trait implementation

#### Fold based version

```
impl Functor Option {
    def fmap f = self fold { None } { Some $ f $1.v }
}

impl Applicative Option {
    def pure = Some
    def (<*>) a = self fold { None } { $1 v fmap a }
}

impl Monad Option {
    def join = self fold { None } { $1 v }   
}

// Functor Option pure 1 fmap (1 +)   
```

#### Smart lookup based version

```
impl Functor Option {
    def None.fmap f = None
    def Some.fmap f = Some $ f self.v
}

impl Applicative Option {
    def pure = Some
    def None.(<*>) a = None
    def Some.(<*>) a = self v fmap a
}

impl Monad Option {
    def None.join = None    
    def Some.join = self v    
}
```

### Trait implementation in action

```
// for FP addicts
+ <$> (pure 1) <*> (pure 1) 

// for OO with FP flavor addicts
+.<$>(pure 1).<*>(pure 1)
```

## 5. Modular system based on files

**Keyword**: Trait based code organisation

### File as trait

Each file containing Lambë code is a trait definition. For instance
a file named `list` can be defined by:
```
data Nil
data Cons a {
    h: a
    t: List a
}
type List a = Nil | Cons a

sig (::) : a -> List a -> List a
def (::) = Cons

// :: 1 Nil
```

This file content is in fact similar to the trait:
```
trait list {
    data Nil
    data Cons a {
        h: a
        t: List a
    }
    type List a = Nil | Cons a

    sig (::) : a -> List a -> List a
    def (::) = Cons
}
```

This implies the capability to use list as a type elsewhere in the code
but also the capability to define trait, type etc. in a trait or it's
implementation.

### Generalising trait approach

If a file is a trait we can also reuse the `for` directive for each function.
```
trait list {
    data Nil
    data Cons a {
        h: a
        t: List a
    }
    type List a = Nil | Cons a

    sig (::) : self -> List a -> List a for a
    def (::) = Cons self

    // 1 :: Nil == 1.(::) Nil
}
```

### Using trait

How this trait can be used in another file? Simple! Just provide an implementation or require its definitions

#### `Global` trait implementation usage

```
impl list

sig isEmpty : self -> Bool for List a
def Nil.isEmpty = true
def Cons.isEmpty = false
```

#### `Local` trait implementation usage

Note: Work in progress

```
sig l : list
def l = impl list

sig isEmpty : self -> Bool for l List a
def (l Nil).isEmpty = true
def (l Cons).isEmpty = false
```

### `Abstract` trait

Since a file is a trait it can also define signatures without implementation.
Therefore the definition should be done when the implementation is required.

For instance the `::` is specified but not defined:
```
data Nil
data Cons a {
    head: a
    tail: List a
}
type List a = Nil | Cons a

sig (::) : self -> List a -> List a for a
```

This trait then can be used but the function `::` implementation is mandatory.

```
impl list {
    def (::) = Cons self
}
```

## 6. Required implementation

### Definition and requirements

```
trait Error a for a {
    raise : self -> b
}

sig div : Int -> Int -> Int with Error String

def div x y =
    if (y == 0)
    then { "divide by zero!" raise }
    else { x / y }
```

### Global scope

The implementation of `Error String` can be provided at the upper level. Then each expression requiring such `Error String` refers to the same implementation.

```
impl Error String {
    def raise = 0
}

div 3 0 // refers to the previous implementation
```

### Local scope

The following code be embedded in a basic block limiting the scope of the provided implementation.

```
let impl Error String {
        def raise = 0
    } in
    div 3 0 // refers to the previous implementation (local scope)
```


### Requiring trait

In trait definition some traits can be required thanks to the `with` keyword. 

```
with list

sig (++) : self -> self -> self for List a

def (++) l = 
    when self {
        is Nil  -> l
        is Cons -> self.h :: $ self.t ++ l
    }
```

In this sample the `::` function is used but not implemented. 

## 7. Examples

### Peanos' Integer

```
data Zero
data Succ { v:Peano }
type Peano = Zero | Succ

trait Adder a for a {
    sig (+) : self -> self -> self
}

impl Adder Peano {
    def Zero.(+) a = a
    def Succ.(+) a = Succ (self v + a)
}
```

```
Succ Zero + $ Succ Zero
```

### if/then/else DSL

```
data If {
    cond : Bool
}

data Then a {
    cond : Bool
    then : Unit -> a
}

sig if : Bool -> If
def if = If

impl for If {
    sig then : self -> (Unit -> a) -> Then a

    def then t = Then self.cond t
}

impl for Then a {
    sig else : self -> (Unit -> a) -> a

    def else f = self cond fold { self then () } { f () }
}

// if                                 : Bool -> If
// if (a > 0)                         : If
// if (a > 0) then                    : (Unit -> a) -> Then a
// if (a > 0) then { a-1 }            : Then Int
// if (a > 0) then { a-1 } else       : (Unit -> Int) -> Int
// if (a > 0) then { a-1 } else { a } : Int
```

### switch/case/otherwise DSL

```
/*
 * Language syntactic extension
 *
 *     switch e
 *     case c1   => e1
 *     case c2   => e2
 *     otherwise => e3
 */

type Predicate a = a -> Bool

sig is : Eq a -> Predicate a
def is a b = a == b

sig switch : a -> Switch a b
def switch a = Switch a None

data Switch a b {
    value  : a
    result : Option b
}

data Case a b {
    value  : a
    result : (Unit -> b) -> Option b
}

data Otherwise b {
    result : (Unit -> b) -> b
}

impl for Switch a b {
    sig case      : self -> Predicate a -> Case a b
    sig otherwise : self -> Otherwise a b

    def case p = Case self.value $ self.result
                                   fold { p self.value fold { Some $ $1 () } { None } }
                                        { self.result }
    def otherwise = Otherwise self.result
}

impl for Case a b {
    sig (=>) : self -> (Unit -> b) -> Switch a b

    def (=>) f = Switch self.value $ self.result f
}

impl for Otherwise b {
    sig (=>) : self -> (Unit -> b) -> b

    def (=>) f = self.result () fold { f () } id
}

// switch 1                                                : Switch Int b
// switch 1 case                                           : Predicate Int -> Case Int b
// switch 1 case (is 0)                                    : Case Int b
// switch 1 case (is 0) =>                                 : (Unit -> b) -> Switch Int b
// switch 1 case (is 0) => { true }                        : Switch Int Bool
// switch 1 case (is 0) => { true } otherwise              : Otherwise Bool
// switch 1 case (is 0) => { true } otherwise =>           : (Unit -> Bool) -> Bool
// switch 1 case (is 0) => { true } otherwise => { false } : Bool
```

### Collection builder

#### Collection builder Data

```
data CollectionBuilder b a {
    unbox : b
    add   : a -> CollectionBuilder b
}
```

#### Collection builder trait

```
trait OpenedCollection b a {
    sig ([)   : self -> a -> ClosableCollection b a
    sig empty : self -> b
}

trait ClosableCollection b a {
    sig (,) : self -> a -> ClosableCollection b a
    sig (]) : self -> b
}
```

#### Collection builder implementation

```
impl OpenedCollection b a for CollectionBuilder b a {
    def ([) a = self add a
    def empty = this unbox
}

impl ClosableCollection b a for CollectionBuilder b a {
    def (,) a = self add a
    def (])   = self unbox
}
```

#### The list builder

```
data Nil
data Cons a {
    h: a
    t: List a
}
type List a = Nil | Cons a

sig List : OpenedCollection (List a) a
def List _ =
    let builder l = CollectionBuilder l { builder $ Cons $1 l } in
    	builder Nil
```

### The List builder in action

```
List      : OpenedCollection (List a) a
List[     : a -> ClosableCollection (List a) a
List[1    : ClosableCollection (List Int) Int
List[1,   : Int -> ClosableCollection (List Int) Int
List[1,2  : ClosableCollection (List Int) Int
List[1,2] : List Int
```

## 8. Grammar

```
s0        ::= entity*

entity    ::= sig | def | data | type | trait | impl | with

sig       ::= "sig" dname ":" type for? with* 
def       ::= "def" (self  ".")? dname  param* "=" expr
data      ::= "data" IDENT t_param* ("{" attr_elem* "}")?
type      ::= "type" IDENT t_param "=" type_expr
trait     ::= "trait" IDENT t_param* with* for? ("{" entity* "}")?
impl      ::= "impl" IDENT t_param* with* for? ("{" entity* "}")?
with      ::= "with" type_out
for       ::= "for" type_out

self      ::= IDENT
            | "(" IDENT* ")" // WIP

expr      ::= "{" (param+ "->")? expr "}"
            | "let" IDENT param* "=" expr "in" expr
            | "let" impl "in"
            | "when" ("let" IDENT =)? expr "{" cases+ "}"
            | param
            | native
            | "_"
            | expr expr
            | "(" expr ")"
            | dname
            | OPERATOR
            | expr "." dname
            | expr "with" ("IDENT "=" expr)+
            | impl
            
case      ::= "is" IDENT "->" expr            

type_expr ::= type_in "->" type_out
            | "(" type_expr ")"
            | type_out
            | "self"
            | data
            | type_expr "|" type_expr

type_in   ::= i_param
            | type_out
            | "(" type_expr ")"

type_out  ::= "(" type_expr ")"
            | o_param type_out?

attr_elem ::= IDENT ":" type

t_param   ::= i_param | o_param
i_param   ::= "(" IDENT ":" type ")"
o_param   ::= IDENT
param     ::= IDENT
dname     ::= IDENT | "(" OPERATOR ")"
native    ::= STRING | DOUBLE | INT | FLOAT | CHAR

IDENT     ::= [a-zA-Z$_][a-zA-Z0-9$_]* - KEYWORDS
KEYWORDS  ::= "sig"   | "def"   | "data"
            | "type"  | "trait" | "impl"
            | "with"  | "for"   | "let" 
            | "in"    | "self"  | "when"
            | "is"

OPERATOR  ::= ([~$#?,;:@&!%><=+*/|_.^-]|\[|\])* - SYMBOLS
SYMBOLS   ::= "(" | ")" | "{" | "}" | "->" | ":" | "." | "|"
```

# Why Lambë?

See [Lambë](https://www.elfdict.com/w/lambe) definition. May be also because it has the same prefix as lambda 😏

# License

Copyright 2019 D. Plaindoux.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
