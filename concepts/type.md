# Type definition

**Kind** : Type specification

## Grammar
```
type  ::= type "->" type ("for" type)?        
        | "self"
        | type op type
        | type type
        | forall param+.type
        | exist param+.type
        | data (ident) attr*
        | type "|" type        
        | "trait" for? with* ("{" entity* "}")?
        | type "." id
        | ident
        
ident ::=          
        | id
        | "(" op ")"   
        
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
int ~> string
```

### Accessing types 

```
trait F (m:type->type) {
    type G = forall a.m a -> a 
}

type ListG = (F List).G
```



