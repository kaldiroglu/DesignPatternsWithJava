# Interpreter — a rule language for a shop

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Interpreter is taught as a short section of the Visitor deck, not as a deck of its own.
Design Patterns, pp. 243–255.

A shop writes its discount rules in a small language: "category is books and price below
50", joined with `and`, `or` and `not`. Each grammar rule is one class, and a rule is a
tree of these objects — a Composite. Each class interprets its own part against a product.

| Class | GoF participant | What it does |
|---|---|---|
| `Rule` | AbstractExpression | `interpret(Product)` and `describe()` |
| `CategoryIs`, `PriceBelow` | TerminalExpression | Test one fact about the product |
| `And`, `Or`, `Not` | NonterminalExpression | Combine other rules |
| `Product` | Context | What the rules are interpreted against |
| `Main` | Client | Builds the tree and interprets it |

The tree is built by hand. Turning the text of a rule into a tree is parsing, which the
pattern does not cover.

The operations are inside the rule classes. With more operations, GoF suggest visitors;
the Visitor deck's homework `hw.expression` is a tree of the same kind, with an evaluator
written as a visitor.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.interpreter.Main
```
