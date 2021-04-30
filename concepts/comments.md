# Structured documentation

**Kind** : Enriched documentation

## Grammar 

```
Comment ::= "--"  Item* "\n"
          | "--{" (Item | "\n")* "}"
          
Item ::= Text - {"```", "\n"}
       | "```" code "```"           
```