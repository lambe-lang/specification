# Lambë 

Strong typed actor based and functional programming language

# Introduction

TODO

# Basic Concepts

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
