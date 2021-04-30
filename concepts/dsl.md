# Domain Specific language

**Kind** : Expression extension

## if/then/else expression

```
syntax if <p> then <a> else <b> { p cond a b () }
```

### Monadic let binding

```
syntax let* <a> = <b> in <c> = b >>= { a -> c } 
```
