<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.basic` — the pattern with the domain taken away

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Deliberately abstract. No windows, no notifications, no shapes — just the four roles and the
counting argument, for the moment in a session when someone asks "yes, but what *is* it".

Use it either as a first look before `bridge.gof`, or as a summary afterwards.

## The two packages

| | `problem` | `pattern` |
|---|---|---|
| Where the implementation lives | a **subclass** of the refinement | a **field** on the refinement |
| 2 refinements × 2 implementations | 4 leaf classes + 2 refinements = **6** | 2 + 2 = **4** |
| A third implementation | **+2** classes | **+1** class |
| Switch implementation on a live object | impossible | pass a different one, or add a setter |

`AnotherSubAbstraction` exists in both packages for exactly one reason: with a single
refinement there is no product to see, and the naive design looks perfectly reasonable. The
second refinement is what turns 2 + 2 into 2 × 2.

## Read it with

`bridge.violation` — two classes showing what else goes wrong when a subclass supplies the
implementation. It is the argument for this pattern, from the other direction.

## Run it with

```bash
mvn -o test -Dtest=BasicBridgeTest

java -cp target/classes dev.kaldiroglu.dp.structural.bridge.basic.solution.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.basic.problem.Main
```
