# Lambë 

Strong typed actor based and functional programming language

# Basic FP Concepts

## Function composition

```
def (a) (b) (c) compose : (a -> b) -> (b -> c) -> a -> c
def compose f g = x -> g $ f x
```

## Trampoline definition

```
data Trampoline : type -> type
data (a) Done : a -> Trampoline a
data (a) Next : (Unit -> Trampoline a) -> Trampoline a
```
## Runnable definition

```
trait Runnable (a) {
    def run : a
}
```
## Runnable Trampoline implementation

```
define (a)(b) Runnable a for Trampoline a {
    def self(Done a) run = a
    def self(Next f) run = f unit run
}
```

## Usage

```
def fact : Int -> Int

def fact i = factTrampoline i 1 run

def factTrampoline : Int -> Int -> Trampoline Int

def factTrampoline 0 acc = Done acc
def factTrampoline n acc = Next $ _ -> factTrampoline (n - 1) (n * acc)
```

# Advanced FP Concepts

## Traits

``` 
trait Functor (m:type->type) (a:type) for m a {
  def (b:type) map : (a -> b) -> m b
}

trait Applicative (m:type->type) (a:type) with Functor m a {
  def (b:type) (<*>) : m (a -> b) -> m b
}

trait Monad (m:type->type) (a:type) with Applicative m a {
  def (b:type) (>>=) : (a -> m b) -> m b
}
```

## Data

```
data Option : type -> type
data (a:type) None : Option a
data (a:type) Some : a -> Option a
```

## Traits definition

```
define (a:type) Functor Option a {
  def self(None)   map _ = None
  def self(Some v) map f = Some (f v)
}

define (a:type) Applicative Option a {
  def (<*>) None     = None
  def (<*>) (Some f) = self map f
}

define (a:type) Monad Option a {
  def self(None)   (>>=) _ = None
  def self(Some v) (>>=) f = f v
}
```

## Usage

```
Some 1 map $ 1 +                    // Some 2, of type Option Int 
Some 1 <*> $ Some $ 1 +             // Some 2, of type Option Int 
Some 1 >>= $ i -> Some $ 1 + i      // Some 2, of type Option Int 
```

# Why Lambë

See [Lambë](http://tolkiengateway.net/wiki/Lambë) definition.

# License

Copyright 2018 D. Plaindoux.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
