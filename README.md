# Lambë 

Strong typed actor based and functional programming language

# Functional Programming Paradigm

## Basic FP Concepts

### Function composition

```
def compose : (a -> b) -> (b -> c) -> a -> c
def compose f g = x -> g $ f x
```

### Trampoline definition

```
data Trampoline : type -> type
data Done : a -> Trampoline a
data Next : (Unit -> Trampoline a) -> Trampoline a
```
### Runnable definition

```
trait Runnable (m:type->type) {
    def run : m a -> a
}
```
### Runnable Trampoline implementation

```
define Runnable Trampoline {
    def run (Done a) = a
    def run (Next f) = f unit run
}
```

### Usage

```
def fact : Int -> Int

def fact i = factTrampoline i 1 run

def factTrampoline : Int -> Int -> Trampoline Int

def factTrampoline 0 acc = Done acc
def factTrampoline n acc = Next $ _ -> factTrampoline (n - 1) (n * acc)
```

## Advanced FP Concepts

### Traits

``` 
trait Functor (m:type->type) {
  def fmap : m a -> (a -> b) -> m b
}

trait Applicative (m:type->type) {
  def (<*>) : m (a -> b) -> m a -> m b
  def lift2 : (a -> b -> c) -> m a -> m b -> m c
}

define (m:type->type) Applicative m when Functor m {
  def lift2 f a = a fmap f <*> 
}

trait Monad (m:type->type) {
  def (>>=) : m a -> (a -> m b) -> m b
}
```

### Data

```
data Option : type -> type
data None : Option a
data Some : a -> Option a
```

### Traits definition

```
define Functor Option {
  def fmap None     _ = None
  def fmap (Some v) f = Some (f v)
}

define Applicative Option {
  def (<*>) None     _ = None
  def (<*>) (Some f) v = v fmap f
}

define Monad Option {
  def (>>=) None     _ = None
  def (>>=) (Some v) f = f v
}
```

### Usage

```
Some 1 fmap $ 1 +                    // Some 2, of type Option Int 
Some (1 +) <*> $ Some 1             // Some 2, of type Option Int 
Some 1 >>= $ i -> Some $ 1 + i      // Some 2, of type Option Int 
```

# Actor Paradigm

TODO

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
