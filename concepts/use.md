# Using definition

**Kind** : Directive

## Description 

The `use` directive opens a trait implementation.

In file `neutral.lambe`
```
trait Neutral a {
    sig neutral : a
}

impl Neutral Int {
    def neutral = 0
}
```

In another source code this can be used thanks to 
the `use` directive:

```
use neutral
```

## Local use in the code source

The `use` directive can opens a trait implementation locally.

```
trait Neutral a {
    sig neutral : a
}

use impl Neutral Int {
    def neutral = 0
}
```

or 

```
trait Neutral a {
    sig neutral : a
}

impl Neutral Int {
    def neutral = 0
}

use Neutral Int
```

## Local use in an expression

```
trait Neutral a {
    sig neutral : a
}

impl Neutral Int {
    def neutral = 0
}

sig zero : () -> int
sig zero = 
    let use Neutral int
    in { neutral } 
```

## Opening a trait specification

If the specified element to be opened is a trait it 
can be used directly if an implementation can be performed 
seamlessly.

```
trait NeutralInt {
    sig neutral : Int
    def neutral = 0
}

use NeutralInt
```

In fact this code is equivalent to:

```
trait NeutralInt {
    sig neutral : Int
    def neutral = 0
}

use impl NeutralInt
```
