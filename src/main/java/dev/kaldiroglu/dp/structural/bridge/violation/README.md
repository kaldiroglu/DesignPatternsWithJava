<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.violation` — why the implementation does not belong in a subclass

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Two classes and a demo. **This is not a Bridge** — it is the argument for one.

`AType` publishes a contract: calling `doIt()` prints something. `ASubType` overrides
`doIt()` to **store** a string instead, and adds `writeIt()` to print it later.

Everything compiles. Every test written against `ASubType` passes. The damage is done to
callers holding an `AType`: they were promised output, and they get silence — no exception,
no log line, no way to tell except by testing the type, which is the thing polymorphism was
supposed to remove. That is a breach of the **Liskov Substitution Principle**.

```
A caller holding AType, expecting each doIt() to print:
  AType    -> My variable: 42
  ASubType ->
```

There is a second broken promise in the same move: `aStringVariable` is only assigned by the
override, so `writeIt()` before `doIt()` prints `null`.

## Why it sits in the Bridge package

Subclassing was used here to change **how** something is done — which is what an
implementation is for. Overriding to do that can break a contract the supertype made, and
break it silently.

Delegation cannot. In `bridge.basic.pattern`, `ASubAbstraction.doIt()` runs its own body and
*then* calls `implementation.doingIt()`. Whatever the implementation does, the refinement's
own contract is still executed, because there is no override to remove it. That difference is
the whole argument for putting the implementation behind a reference rather than above it in
a hierarchy.

## Run it with

```bash
mvn -o test -Dtest=ViolationTest
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.violation.Main
```
