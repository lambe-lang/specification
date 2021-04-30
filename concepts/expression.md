# Expression

**Kind** : Expression definition

## Grammar 
```
expr  ::= "{" (id+ "->")? expr "}"
        | "let" id (param)* "=" expr "in" expr
        | "let" "impl" type_expr "in" expr
        | "when" ("let" id = expr | id) case+
        | id
        | op
        | expr expr
        | "(" expr ")"
        | expr "." expr
        | expr "as" type
        | "unpack" "{" id "," id "}" "=" exp "in" exp
        | "pack "{" type "," exp "}"
        | impl
        
case  ::= "is" type "->" expr          
```