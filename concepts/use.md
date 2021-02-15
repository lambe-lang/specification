# Using definition

**Kind** : Directive

## Description 

The `use` directive opens a trait implementation.

In file `neutral.lambe`
```
trait Neutral a {
    sig zero : a
}

impl Neutral Int {
    def zero = 0
}
```

In another source code this can be used thanks to 
the `use` directive:

```
use neutral
```

## Local use

The `use` directive can opens a trait implementation locally.

```
trait Neutral a {
    sig zero : a
}

use impl Neutral Int {
    def zero = 0
}
```

or 

```
trait Neutral a {
    sig zero : a
}

impl Neutral Int {
    def zero = 0
}

use Neutral Int
```

## Opening a trait

If the specified element to be opened is a trait it 
can be used directly if an implementation can be performed 
seamlessly.

```
trait NeutralInt {
    sig zero : Int
    def zero = 0
}

use NeutralInt
```

In fact this code is equivalent to:

```
trait NeutralInt {
    sig zero : Int
    def zero = 0
}

sig implNeutralInt : NeutralInt 
def implNeutralInt = impl NeutralInt
use implNeutralInt
```


