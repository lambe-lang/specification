# Lambë 

[![experimental](http://badges.github.io/stability-badges/dist/experimental.svg)](http://github.com/badges/stability-badges)

Strong typed actor based and functional programming language

# Introduction

TODO

# Basic Concepts

## Traits

``` 
trait Functor[m:type->type][a:type] {
  map [b:type] (a -> b) -> m b
}

trait Applicative[m:type->type][a:type] with Functor m a {
  (<*>) [b:type] m (a -> b) -> m b
}

trait Monad[m:type->type][a:type] with Applicative m a {
  flatmap [b:type] (a -> m b) -> m b
}
```

## Data

```
data Option[a:type] : type
data None[a:type] : Option a
data Some[a:type](v:a) : Option a
```

## Trait implementation

```
define Functor Option a for Option when None {
  map _ = None
}

define Functor Option a for Option when Some {
  map f = Some (f v)
}

define Applicative Option a for Option a {
  (<*>) None     = None
  (<*>) (Some f) = self map f
}

define Monad Option a for Option a when None {
  flatmap _ = None
}

define Monad Option a for Option a when Some {
  flatmap f = f v
}
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
