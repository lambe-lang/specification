# Type definition

## Grammar
```
type  ::= type "->" type ("for" type)?        
        | "self"
        | type type
        | forall param+.type
        | exist param+.type
        | data id attr*
        | type "|" type        
        | "trait" for? with* ("{" entity* "}")?
        | type "." id    
        
attr  ::= "(" id : type ")"
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
forall a.data Cons (h:a) (t:list a) | data Nil
trait { sig add : self -> int -> int for self }
trait { type t : int }.t
```

### Accessing types 

```
trait F (m:type->type) {
    type G = forall a.m a -> a 
}

type ListG = (F List).G
```



