# Lambë

A strong typed functional programming inspired by Haskell, OCaml and Rust.

## Type definition

```
sig id   : a -> a
sig swap : (a -> b -> c) -> b -> a -> c
sig (°)  : (b -> c) -> (a -> b) -> a -> c
sig (|>) : (a -> b) -> (b -> c) -> a -> c
```

##  Function definition

```
def id   = a -> a
def swap = f x y -> f y x		
def (°)  = f g x -> f (g x)
def (|>) = swap (°)
 ```

## Data type definition

```
type Option a {
    None
    Some v:a
}
```

## Direct implementation

```
impl for Option a {
     sig fold: self -> (None a -> b) -> (Some a -> b) -> b

     def None.fold n _ = n self // self : None a
     def Some.fold _ s = s self // self : Some a
}

// Some 1 fold 0 id = 1 : int // for FP addicts
// (Some 1).fold 0 id   : int // for OO addicts
```

## Trait definition

```
trait Functor (f:type->type) {
    sig fmap : self -> (a -> b) -> f b for f a
}

trait Applicative (f:type->type) with Functor f {
    sig pure : a -> f a
    sig <*>  : self -> f (a -> b) -> f b for f a
}

trait Monad (f:type->type) with Applicative f {
    sig join  : self -> f a for f (f a)
    sig (>>=) : self -> (a -> f b) -> f b for f a

    def (>>=) f = self fmap f join
}
```

```
impl Functor Option{
    def fmap f = self fold { None } { Some $ f _.v }
}

impl Applicative Option {
    def pure a = Option a
    def (<*>) f = f fold { None } { a fmap _.v }
}

impl Monad Option {
    def join = self fold None id
    def (>>=) f = self fold { None } { f _.v } // specific
}

// Some 1 fmap (1+)            : Option int
// (Applicative Option) pure 1 : Option int
```

## Peanos' integer

```
type Peano {
    Zero
    Succ v:Peano
}

trait Adder a {
    sig (+) : self -> self -> self for a
}

impl Adder Peano {
    def Zero.(+) a = a
    def Succ.(+) a = Succ (self v + a)
}

// (Succ Zero) + (Succ Zero)
```

## designing a DSL

### Collection builder

```
data CollectionBuilder b a {
    unbox : b
    add   : a -> CollectionBuilder b
}

trait OpenedCollection b a {
    sig ([)   : self -> a -> ClosableCollection f a
    sig empty : self -> f a
}

trait ClosableCollection b a {
    sig (,) : self -> a -> ClosableCollection b a
    sig (]) : self -> b
}
```

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

### The list builder

```
type List a {
    Nil
    Cons h:a t:(List a)
}

impl for List a {
    sig (+:) : self -> a -> List a

    def (+:) a = Cons a self
}

sig List : OpenedCollection (List a) a
def List =
    let listBuider l = CollectionBuilder l { listBuilder $ l +: _ } in
    	listBuilder Nil

// List[     : a -> ClosableCollection (List a) a
// List[1    : ClosableCollection (List int) int
// List[1,   : int -> ClosableCollection (List int) int
// List[1,2  : ClosableCollection (List int) int
// List[1,2] : List int
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
