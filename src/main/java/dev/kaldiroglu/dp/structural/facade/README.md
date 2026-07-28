<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `facade` — the Facade pattern

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Facade,
pp. 185–193.

> Provide a unified interface to a set of interfaces in a subsystem. Facade defines a
> higher-level interface that makes the subsystem easier to use.

| Package | Example |
|---|---|
| `gof` | The book's own: a compiler whose scanner, parser, builder and code generators most clients never want to see |
| `notification` | Five services behind one call — with `problem`, `solution1` and `solution2` side by side |
| `slf4j` | The facade everybody has already used, where the subsystem is chosen at deployment |
| `hw` | Worked homework: partial failure, a leaky facade, and one where the knowledge is the call order |

## The sentence worth keeping

A facade is **a front door, not a wall**. GoF are explicit (p. 185): it "doesn't prevent
applications from using subsystem classes if they need to." A class that hides a subsystem so
thoroughly that no one can reach past it has stopped being a facade and become a bottleneck.

Both halves are asserted: `theSubsystemStaysReachable` builds the parse tree by hand with a
`RiscCodeGenerator`, and `oneCall` does the same work in one line.

## How to tell it is really a facade

Look at what its **clients have to name**. If the signatures return subsystem types, callers
still import the subsystem, and a layer has been added without a dependency being removed —
which is exactly what `hw.reporting.LeakyReportFacade` exists to show.

## What it costs

- **A facade over several subsystems inherits partial failure.** `hw.checkout` reserves
  stock, then the payment is declined; if the facade does not put the stock back, every
  caller needs the subsystem knowledge the facade was meant to remove.
- **It can grow into a god object.** One class that everything calls and that calls
  everything is not simpler, only differently arranged.

## Run it with

```bash
mvn -o test -Dtest='CompilerFacadeTest,NotificationFacadeTest,LoggingFacadeTest,CheckoutFacadeTest,ReportFacadeTest,VideoConverterTest'

java -cp target/classes dev.kaldiroglu.dp.structural.facade.gof.Client
java -cp target/classes dev.kaldiroglu.dp.structural.facade.notification.problem.Client
java -cp target/classes dev.kaldiroglu.dp.structural.facade.notification.solution2.Client
java -cp target/classes dev.kaldiroglu.dp.structural.facade.slf4j.Main
```

> The demo classes here are named `Client` rather than `Main`, unlike the rest of the
> repository. That is deliberate: **Client** is a participant in this pattern, so the name
> carries meaning.
