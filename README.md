# Lambë

A statically typed functional programming language inspired by Haskell, OCaml, Rust and Kotlin.

## Preamble

A [formalism](https://github.com/lambe-lang/specification/blob/master/formalism/lambe.pdf) for Lambë is in progress. 

## 0. Paradigms

Targeted programming language paradigms for the design of Lambë are:
- [X] Functional programming,
- [X] Static typing,
- [X] Higher-kinded-type,
- [X] Smart cast
- [X] Algebraic Data Type aka ADT.  
- [X] Trait based code organisation,
- [X] Trait specification as first class type citizen,
- [X] Trait implementation as first class term citizen,
- [X] Self receiver concept,
- [X] Coarse and fine grain self specification i.e. receiver type,
- [X] Structured comments
- [ ] Algebraic effects 

## 1. Function

**Keyword**: Functional programming, Static typing

### 1.1 Basic concept

#### Definition

```
sig id       : forall a. a -> a
sig swap     : forall a b c.(a -> b -> c) -> b -> a -> c
sig compose  : forall a b c.(b -> c) -> (a -> b) -> a -> c
sig pipeline : forall a b c.(a -> b) -> (b -> c) -> a -> c
```

#### Implementation

```
def id       = { a -> a }         // equivalent to { _1 }
def swap     = { f x y -> f y x } // equivalent to { _1 _3 _2 }
def compose  = { _1 $ _2 _3 }     // equivalent to { f g x -> f (g x) }
def pipeline = swap compose
 ```

### 1.2 Self concept

**Keyword**: Self receiver concept

A function can be specified with a `self` type at the first position. Therefor such function is infix and accept the
dot notation. The self type is define by the attached `for` directive.

#### Definition

```
sig ($)  : forall a b c. self -> (a -> b) -> a -> c for b -> c
sig (|>) : forall a b c. self -> (b -> c) -> a -> c for a -> b
```

#### Implementation

```
def ($) f a = self (f a)
def (|>) f = f $ self
 ```

#### Function with self definition in Action

```
1 + $ 3 + 4
3 + |> 4 + |> 2 * 5  
```
 
## 2. Data type

**Keyword**: Algebraic Data Type

### 2.1 Data type definition

#### 2.1.1 Split definitions

```
data None
data Some a (value: a)
type Option a = None | Some a
```

For each data a corresponding constructor is define denoted by a function where parameters are define thanks to attribute
specification based order:

```
sig None : None
sig Some : forall a.a -> Some a
```

Lambë does not provide a pattern matching, but a Kotlin like smart cast on types.

```
// Given an optional o
when o
is None -> // o is a data None
is Some -> // o is a data Some
``` 

#### 2.1.2 Sealed definitions

```
type Option a = 
  data None 
| data Some (value: a)
```

For each data a corresponding constructor is define denoted by a function where parameters are define thanks to attribute
specification based order:

```
sig None : forall a.Option a
sig Some : forall a.a -> Option a
```

The smart cast does not change since `data Some (value : a)` 

### 2.2 Data type implementation

```
impl forall a. Option a {
    sig fold: self -> b -> (a -> b) -> b

    def fold n s = 
        when self
        is None -> n
        is Some -> s self.value       
}
```

In this implementation for `Option a` we use a type named `self`. In fact self denotes the type of the receiver which is `Option a` in this case defined thanks to the `for ...` declaration. Furthermore, implementations are
define for each option data type i.e. None and Some.

### 2.3 Data type in action

```
Some 1 fold { 0 } id
```

## 3. Traits

**Keyword**: Trait based code organisation

### Trait definition

```
trait Functor (f:*->*) {
    sig map   : self -> (a -> b) -> f b for f a
    sig (<$>) : self -> f a -> f b for a -> b
    
    def (<$>) a = a map f
}
```

The `Functor` has a parametric type constructor `f` revealing the support of higher-kinded-types in the language.

The `map` has a receiver called `self` and this receiver has the type `f a` given by the *for* directive.

```
trait Applicative (f:*->*) with Functor f {
    sig pure   : a -> f a
    sig (<*>)  : self -> f a -> f b for f (a -> b)
    sig (<**>) : self -> f (a -> b) -> f b for f a

    def (<**>) a = a <*> self
}
```

Such *for* directive can be defined at the trait level, signature level or definition level. If such a directive is not 
expressed for a method and does not have `self` as first parameter it's a *static* method. 

```
trait Monad (f:*->*) with Applicative f {
    sig return : a -> f a 
    sig join   : self -> f a for f (f a)
    sig (>>=)  : self -> (a -> f b) -> f b for f a
    sig (=<<)  : self -> f a -> f b for a -> f b

    def return  = pure
    def (>>=) f = self map f join
    def (=<<) a = a >>= self
}
```

Finally, each method can be specified with a dedicated `self` type. As a conclusion, a trait define a logical development unit.

### Trait implementation

#### Fold based version

```
impl Functor Option {
    def map f = self fold { None } { Some $ f _1.v }
}

impl Applicative Option {
    def pure = Some
    def (<*>) a = self fold { None } { _1 value map a }
}

impl Monad Option {
    def join = self fold { None } { _1 value }   
}

// Functor Option pure 1 map (1 +)   
```

#### Smart lookup based version

```
impl Functor Option {
    def map f = 
      when self is
       is None -> None
       is Some -> Some $ f self.v
}

impl Applicative Option {
    def pure = Some
    def (<*>) a = self fold {None} { _ value map a }
}

impl Monad Option {
    def join = self fold {None} { _ value }    
}
```

### Trait implementation in action

```
+ <$> (pure 1) <*> (pure 1) 
```

## 4. Modular system based on file

**Keyword**: Trait based code organisation

### File as trait

Each file containing Lambë code is a trait definition. For instance a file named `list` can be defined by:

```
data Nil
data Cons a (h: a) (t: List a)

type List a = Nil | Cons a

sig (::) : forall a. a -> List a -> List a
def (::) = Cons

// 1 :: Nil
```

This file content is in fact similar to the trait:
```
trait list {
    data Nil
    data Cons a (h: a) (t: List a)
    type List a = Nil | Cons a

    sig (::) : forall a. a -> List a -> List a
    def (::) = Cons
}
```

This implies the capability to use list as a type elsewhere in the code but also the capability to define locally traits, 
types etc. in a trait, or it's implementation.

### Generalising trait approach

If a file is a trait we can also reuse the `for` directive for each function.

```
trait list {
    data Nil
    data Cons a (h: a) (t: List a)
    type List a = Nil | Cons a

    sig (::) : forall a. self -> List a -> List a for a
    def (::) = Cons self
}
```

### Using trait

How this trait can be used in another file? Simple! Just provide an implementation or require its definitions.

#### `Global` trait implementation usage

```
use list

sig isEmpty : forall a. self -> bool for List a
def isEmpty = 
    when self
    is Nil  -> true
    is Cons -> false
```

#### `Local` trait implementation usage

Implementation car also be local using a specific `let` binding.

For instance, we can design a trait which denotes a transformation and an implementation from `Try` to `Option`.

```
trait (~>) (f:*->*) (g:*->*) {
    sig transform: forall a. self -> g a for f a
}

impl Try ~> Option {
    def transform =
        when self
        is Failure -> None
        is Success -> Some self.value
}
```

In another compilation an implementation can be locally and explicitly required. This is done using the specific binding.

```
sig main : forall a. Try a -> Option a
def main t =
    let use Try ~> Option in
        t transform
```

Of course this example requirement can only be specified. Therefore its implementation should be provided by the caller.

```
sig main : forall a.Try a -> Option a with Try ~> Option
def main t = t transform
```

### `Abstract` trait

If a file is a trait, it can also define signatures without implementation.
Therefore, the definition should be given when the implementation is required.

For instance the `::` is specified but not defined:
```
data Nil
data Cons a (h: a) (t: List a)
type List a = Nil | Cons a

sig (::) : forall a. self -> List a -> List a for a
```

This trait then can be used but the function `::` implementation is mandatory.

```
impl list {
    def (::) = Cons self
}
```

## 5. Algebraic Effect

Under consideration

## 6. Required implementation

### Definition and requirements

```
trait Error a {
    raise : forall b. a -> b
}

sig div : int -> int -> int with Error string

def div x y =
    if (y == 0)
    then { raise "divide by zero!" }
    else { x / y }
```

### Global scope

The implementation of `Error string` can be provided at the upper level. Then each expression requiring such `Error string` refers to the same implementation.

```
impl Error string {
    def raise = 0
}

div 3 0 // refers to the previous implementation
```

### Local scope

The following code is embed in a basic block limiting the scope of the provided implementation.

```
def Error string = {
        def raise = 0
    } in
    div 3 0 // refers to the previous implementation (local scope)
```


### Requiring trait

In trait definition some traits can be required thanks to the `with` keyword. It's important 
to notice the difference between `with` which is a requirement to be solved later, and an `import`.  

```
with list

sig (++) : forall a. self -> self -> self for List a

def (++) l = 
    when self
    is Nil  -> l
    is Cons -> self.h :: $ self.t ++ l
```

In this sample the `::` function is use but not implemented. 

## 7. Examples

### if/then/else DSL

```
data If (cond : bool)
data Then a (cond : bool) (then : unit -> a)

sig if : bool -> If
def if = If

impl If {
    sig then : forall a. self -> (unit -> a) -> Then a

    def then t = Then self.cond t
}

impl forall a. Then a {
    sig else : forall b.self -> (unit -> a) -> a | b

    def else f = self cond fold { self then () } { f () }
}

// if                                 : bool -> If
// if (a > 0)                         : If
// if (a > 0) then                    : forall a.(unit -> a) -> Then a
// if (a > 0) then { a-1 }            : Then int
// if (a > 0) then { a-1 } else       : forall b.(unit -> int) -> int | b
// if (a > 0) then { a-1 } else { a } : int
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

type Predicate a = a -> bool

sig is : forall a. a -> Predicate a with Eq a
def is a b = a == b

sig switch : a -> Switch a b
def switch a = Switch a None

data Switch a b (value: a) (result : Option b)
data Case a b (value : a) (result : (unit -> b) -> Option b)
data Otherwise b (result : (unit -> b) -> b)

impl forall a b. Switch a b {
    sig case      : self -> Predicate a -> Case a b
    sig otherwise : self -> Otherwise a b

    def case p = Case self.value $ self.result
                                   fold { p self.value fold { Some $ $1 () } { None } }
                                        { self.result }
    def otherwise = Otherwise self.result
}

impl forall a b. Case a b {
    sig (=>) : self -> (unit -> b) -> Switch a b

    def (=>) f = Switch self.value $ self.result f
}

impl forall b. Otherwise b {
    sig (=>) : self -> (unit -> b) -> b

    def (=>) f = self.result () fold { f () } id
}

// switch 1                                                : Switch int b
// switch 1 case                                           : Predicate int -> Case int b
// switch 1 case (is 0)                                    : Case int b
// switch 1 case (is 0) =>                                 : (unit -> b) -> Switch int b
// switch 1 case (is 0) => { true }                        : Switch int bool
// switch 1 case (is 0) => { true } otherwise              : Otherwise bool
// switch 1 case (is 0) => { true } otherwise =>           : (unit -> bool) -> bool
// switch 1 case (is 0) => { true } otherwise => { false } : bool
```

### Collection builder

#### Collection builder Data

```
data CollectionBuilder a b {
    add   a -> CollectionBuilder a b
    unbox : b
}

data OpenableCollection a b { 
    value : CollectionBuilder a b 
}

data ClosableCollection a b {
    value : CollectionBuilder a b
} 
```

#### Collection builder trait implementations

```
impl forall a b. OpenableCollection a b {
    sig ([)   : self -> a -> ClosableCollection a b
    def ([) a = ClosableCollection $ self value add a

    sig empty : self -> b
    def empty = self value unbox
}

impl forall a b. ClosableCollection a b {
    sig (,) : self -> a -> self
    def (,) a = ClosableCollection $ self value add a

    sig (]) : self -> b
    def (])   = self value unbox
}
```

#### The list builder

```
data Nil
data Cons a (h: a) (t: List a)
type List a = Nil | Cons a

sig List : forall a. OpenableCollection (List a) a
def List =
    let builder = { l -> CollectionBuilder l { builder $ Cons _ l } } in
    	OpenableCollection $ builder Nil
```

### The List builder in action

```
List      : OpenableCollection (List a) a
List[     : a -> ClosableCollection (List a) a
List[1    : ClosableCollection (List int) int
List[1,   : int -> ClosableCollection (List int) int
List[1,2  : ClosableCollection (List int) int
List[1,2] : List int
```

## 8. Grammar

```
s0        ::= entity*

entity    ::= kind | sig | def | data | type | trait | impl | use

use       ::= "use" IDENT
            | "from" IDENT use IDENT ("," IDENT)*
sig       ::= "sig" dname ":" type_expr for? with* 
def       ::= "def" dname param* "=" expr
data      ::= "data" dname t_kind* t_param*
kind      ::= "kind" dname "=" kind_expr
type      ::= "type" dname t_param "=" type_expr ("|" type_expr) 
trait     ::= "trait" IDENT t_param* for? with* ("{" entity* "}")?
impl      ::= "impl" IDENT t_param* for? with* ("{" entity* "}")?
with      ::= "use" type_expr
for       ::= "for" type_expr

kind_expr ::= "*"
            | kind_expr "->" kind_expr
            | "(" kind_expr ")" 

attr_type ::= IDENT ":" type_expr

expr      ::= "{" (param+ "->")? expr "}"
            | "let" IDENT (param)* "=" expr "in" expr
            | "let" "impl" type_expr "in" expr
            | ("when" ("let" IDENT =)? expr)+ cases+``
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
            
case      ::= ("is" type_expr)+ "->" expr            

type_expr ::= type_expr OPERATOR type_expr
            | "(" type_expr | OPERATOR ")"
            | type_expr type_expr
            | type_expr "." dname
            | type_expr "|" type_expr
            | data dname t_param*
            | IDENT 
            | "self"
            | "forall" (attr_kind)+ "." type_expr 
            | "exists" (attr_kind)+ "." type_expr 
            | "self" "->" type_expr "for" type_expr 

attr_kind ::= IDENT
            | "(" IDEND : kind_expr ")"

t_param   ::= "(" IDENT ":" type ")"
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

Copyright 2019-2021 D. Plaindoux.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
