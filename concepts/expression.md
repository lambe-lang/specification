# Expression

## Grammar 
```
expr  ::= "{" (param+ "->")? expr "}"
        | "let" IDENT (param)* "=" expr "in" expr
        | "let" "impl" type_expr "in" expr
        | ("when" ("let" IDENT =)? expr)+ case+``
        | param
        | native
        | "_"
        | expr expr
        | "(" expr ")"
        | dname
        | OPERATOR
        | expr "." dname
        | expr "with" ("IDENT "=" expr)+
        | impl
        
case  ::= "is" type "->" expr          
```
