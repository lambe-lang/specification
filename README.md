# Lambë 

[![Build Status](https://travis-ci.org/d-plaindoux/lambe.svg?branch=master)](https://travis-ci.org/d-plaindoux/lambe?branch=master)
[![experimental](http://badges.github.io/stability-badges/dist/experimental.svg)](http://github.com/badges/stability-badges)

Strong typed actor based and functional programming language

# Introduction

TODO

# Basic Concepts

## Traits

``` 
trait Functor[m:type->type][a:type] {
  def map [b:type] (a -> b) -> m b
}

trait Applicative[m:type->type][a:type] with Functor m a {
  def (<*>) [b:type] m (a -> b) -> m b
}

trait Monad[m:type->type][a:type] with Applicative m a {
  def flatmap [b:type] (a -> m b) -> m b
}
```

## Data

```
data Option[a:type] -> type
data None[a:type] -> Option a
data Some[a:type] a -> Option a
```

## Traits definition

```
define [a:type] Functor Option a for Option a {
  def self(None)   map _ = None
  def self(Some v) map f = Some (f v)
}

define [a:type] Applicative Option a for Option a {
  def (<*>) None     = None
  def (<*>) (Some f) = self map f
}

define [a:type] Monad Option a for Option a {
  def self(None)   flatmap _ = None
  def self(Some v) flatmap f = f v
}
```

## Usage

```
let a = Some 1 map $ 1 +                    // a = Option 2
let b = Some 1 <*> $ Some $ 1 +             // b = Option 2
let c = Some 1 flatmap $ i -> Some $ 1 + i  // c = Option 2
```

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
