# Syntax extension

**Kind** : Expression extension

## if/then/else expression

```
syntax if <p> then <a> else <b> { p fold { a } { b } () }
```

````
if condition a b then expr1 else expr2 
````

## Monadic let binding

```
syntax let* <a=ident> = <b> in <c> { b >>= { a -> c } } 
```

```
let* a = f b in g a
```

## Do notation  

The `do` notation is a based on a recursive construction. For this purpose
the syntax extension can express thanks to the following syntax extension:

```
syntax do <n=ident> <- <a> ; <b=do>   { a >>= { n -> b } }
        | <n=ident> <- <a> yield <b>  { a <$> { n -> b } } 
        | <a> ; <b=do>                { a >>= { b }      }
        | <a> yield <b>               { a <$> { b }      }
```

```
let use Monad Option in
    do n <- pure 30 
     ; r <- (+) <$> n <*> pure 10 
    yield r + 2
```
