# Visitor — a health check in a company

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A company's employees — engineers, secretaries, managers, directors — and its boss get a
health check. `HealthVisitor` checks everyone who has worked more than five years, every
manager's psychological state, and the boss if the boss is over fifty.

| Class | Role |
|---|---|
| `Visitor` | Visitor — `visit(Employee)` and `visit(Boss)` |
| `HealthVisitor` | ConcreteVisitor |
| `Employee` and its subclasses, `Boss` | ConcreteElement |
| `Company` | ObjectStructure — sends the visitor to every employee |
| `HR` | Creates random employees |

Two things to see:

- `Boss` is not an `Employee`. A visitor can visit classes that have no common parent.
- There is one `visit(Employee)` for four classes, so `HealthVisitor` tests
  `instanceof Manager` inside it.

`HR` picks employees with `Math.random()`, so the output changes from run to run.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.factory.Test
```
