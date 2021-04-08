# File & Directory

**Kind**: Trait type

Each source code i.e. file is a trait by definition.

## Use a file

Since a file (and a directory) is a trait it can be used.
Then the rule is the same for a trait implementation a
a file implementation.

File `Map.lambe`
```
kind t = * -> *

sig map : forall a b. self -> t a -> t b for a -> b
```

If such a file is used an implementation for `pure` is required
and an incarnation for the type `t` is also mandatory.

File `r.lambe`
```
use impl Applicative {
    use lang.option

    type t = Option
    
    def map a = 
        when a 
        is None -> None
        is Some -> Some (self a.value) 
}
```

