# Type definition

## Grammar
```
type  ::= type "->" type ("for" type)?
        | "self"
        | type type
        | forall param+.type
        | exist param+.type
        | id "{" (attr (";" attr)* )? "}
        | type "|" type        
        | "trait" for? with* ("{" entity* "}")?    
        
attr  ::= id : type
param ::= id | "(" id ":" kind)        
```

## Examples

```
string -> int
self -> int for string
list string
forall a.list a
exist a.list a
Nil {}
forall a.a | Nil{}
trait { sig add : self -> int -> int for self }
```



