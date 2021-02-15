# Domain Specific language

TODO

## if/then/else expression

```

dsl if <p> then <a> else <b>
    : forall a. bool -> (() -> a) -> (() -> a) -> a   
    = p cond a b ()
```

### Monadic let binding

```
dsl let* <a> = <b> in <c>
    : forall (m:type -> type) a b.ident -> (m a) -> (a -> m b) -> m c with Monad m
    = b >>= { a -> c } 
```