# Visitor — homework 3: an expression tree

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: evaluate and print `(2 + 3) * -4`. Then add a new kind of node. What
changes?

The value is -20. A new node, `Neg`, was one new record and one new `visit` method in
`ExprVisitor`, and so in every visitor. A new operation, `DepthCounter`, was one new class
and no other change. That is GoF's trade-off: new operations are easy, new element classes
are hard.

| Class | Role |
|---|---|
| `Expr` | Element: `accept(visitor)` |
| `Num`, `Add`, `Mul`, `Neg` | Concrete elements, as records |
| `ExprVisitor<R>` | Visitor: one `visit` for each kind of node |
| `Evaluator`, `Printer`, `DepthCounter` | Concrete visitors: the value, the text, the depth |
| `Main` | Builds `(2 + 3) * -4` and runs the three visitors |

## Diagram

![Visitor - Expression (Homework) - Class Diagram](uml/Visitor%20-%20Expression%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`ExpressionTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=ExpressionTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.hw.expression.Main
```
