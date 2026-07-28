<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `composite.hw` — worked solutions to the homework

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Three problems set at the end of the Composite session, solved. **Try them before you read
them.** Each package has a runnable `Main` and a test class.

| Package | Problem | What the code settles |
|---|---|---|
| `orgchart` | Managers, reports, headcount and salary | headcount **7** against a true **6** — the sharing trap, measured |
| `expression` | `(3 + 4) * (10 - 2) / 4` as objects | a composite with exactly two children is still a composite |
| `surveyform` | Sections holding questions and sections | an operation that concatenates, and the bill for transparency |

## 1 · `orgchart`

The roll-up is twenty minutes. The exercise is what follows it: one person reporting to two
managers makes the structure a **graph**, and every roll-up over it silently over-counts.

```
headcount           7   <- the tree walk sees Cem twice
distinctHeadcount   6   <- identity de-duplication gives the truth
total cost    750,000   <- and his salary is paid twice
```

Nothing throws. Nothing warns. This is the liability the pattern will actually hand them at
work, and it is the same one `composite.bom` accepted on purpose when it made the wheel
shareable. A **cycle** is rejected at `add` time, because that one hangs the program rather
than merely lying about the answer.

## 2 · `expression`

Here the tree **is** the data, which is different from the other two: elsewhere the hierarchy
models something in the world, and here the hierarchy *is* the arithmetic.

`BinaryOperation` holds exactly two children rather than a list — a Composite has to hold
**components**, not an unbounded collection of them. `Divide` throws on a zero divisor
instead of inventing a number, because a wrong total is worse than none.

## 3 · `surveyform`

`validate()` returns a **list**, not a number, so the composite concatenates rather than
sums — the shape most real composite operations take, and slightly harder.

This package takes the **transparent** side: `add` is on `FormElement`, so no client ever
tests a type, and `Question.add` must therefore throw. The call that does it **compiles
without complaint**, which is exactly the trade GoF describe and exactly what
`composite.graphic` and `composite.fileSystem` chose to avoid.

## Run it with

```bash
mvn -o test -Dtest='OrgChartTest,ExpressionTest,SurveyFormTest'    # 17 tests

java -cp target/classes dev.kaldiroglu.dp.structural.composite.hw.orgchart.Main
java -cp target/classes dev.kaldiroglu.dp.structural.composite.hw.expression.Main
java -cp target/classes dev.kaldiroglu.dp.structural.composite.hw.surveyform.Main
```
