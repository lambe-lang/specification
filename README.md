# Lambë

A strong typed functional programming

## Type definition

```
sig id   : a -> a
sig swap : (a -> b -> c) -> b -> a -> c
sig (°)  : (b -> c) -> (a -> b) -> a -> c
sig (|>) : (a -> b) -> (b -> c) -> a -> c
```

##  Function definition

```
def id a       = a
def swap f x y = f y x		
def (°) f g x  = f (g x)
def (|>)       = swap (°)
 ```

## Data type definion

```
type Option a {
    None
    Some v:a
}
```

## Direct implementation

```
impl for Option a { // self : Option a
     sig fold: b -> (a -> b) -> b

     def None.fold n _ = n
     def Some.fold _ s = s $ self v // self is a Some
}

// Some 1 fold 0 id = 1 : int // for FP addicts
// (Some 1).fold 0 id   : int // for OO addicts
```

## Trait definition

```
trait functor (f:type->type) a for f a {
     fmap : (a -> b) -> f b
}

impl functor Option a { // for Option a is infered
     def fmap f = self fold None { Some $ f _ }
}

// Some 1 fmap (1+) : Option int

```

## Peanos' integer

```
type Peano {
    Zero
    Succ v:Peano
}

trait Adder a for a {
    sig (+) : a -> a
}

impl Adder Peano {
    def Zero.(+) a = a
    def Succ.(+) a = Succ (self v + a)
}

// (Succ Zero) + (Succ Zero)
```

## DSL

### Collection builder

```
data CollectionBuilder a b {
    unbox : b
    add   : a -> CollectionBuilder a b
}

trait OpenCollectionBuilder a b {
    sig ([)   : a -> CloseCollectionBuilder a b
    sig empty : b
}

impl OpenCollectionBuilder a b for CollectionBuilder a b {
    def ([) a = self add a ;
    def empty = this unbox
}

trait ClosableCollectionBuilder a b {
    sig (,) : a -> ClosableCollectionBuilder a b
    sig (]) : b
}

impl ClosableCollectionBuilder a b for CollectionBuilder a b {
    def (,) a = self add a
    def (])   = self unbox
}
```

### The list builder

```
type List : type -> type
data Nil  : List a
data Cons : h:a -> t:(List a) -> List a

// Alternate syntax
type List a {
    Nil
    Cons h:a t:(List a)
}

impl for List a {
    sig (+:) : a -> List a

    def (+:) a = Cons a self
}

sig List : OpenCollectionBuilder (List a) a
def List =
    let listBuider l = CollectionBuilder l { listBuilder $ l +: _ } in
    	listBuilder Nil

// List[   : a -> ClosableCollectionBuilder a (List a)
// List[1  : ClosableCollectionBuilder int (List int)
// List[1] : List[int]
```

# Why Lambë?

See [Lambë](http://tolkiengateway.net/wiki/Lambë) definition.

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
