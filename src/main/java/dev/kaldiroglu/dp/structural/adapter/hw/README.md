<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `adapter.hw` — worked solutions to the homework

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Three problems, chosen so that between them they cover the mismatches the `electricity` and
`gof` examples do not. **Try them before you read them.** Each package has a runnable `Main`
and a test class.

| Package | The mismatch | What the code settles |
|---|---|---|
| `payments` | names, **data**, and **error model** | only one of the three is about method names |
| `iteration` | operation model, and an **unsupportable** method | `remove` has nothing to map onto |
| `telemetry` | many adaptees, one adapter | GoF's pluggable form, technique (c) |

## 1 · `payments`

A gateway from 2004: integer cents, status codes, and an out-parameter array carrying the
result. Three separate mismatches have to be reconciled and only the first is renaming:

1. **Names** — `submitTxn` becomes `charge`. Trivial.
2. **Data** — `BigDecimal` lira become integer cents, and the rounding rule is a decision
   somebody must make and write down.
3. **Error model** — integer codes become exceptions.

The third carries the risk, and the riskiest line is the `default` case. **A code the adapter
has never seen must still be a failure.** Translating only the codes you know is how a decline
becomes a delivered order — `unrecognizedCodesFailClosed` pins it.

## 2 · `iteration`

An in-house cursor from before `Iterator` existed. `hasMoreElements`/`nextElement` map cleanly
onto `hasNext`/`next`, and then `remove` has **nothing to map onto** — a `Cursor` has no notion
of removal and no translation invents one.

> GoF, p. 141: how useful an adapter is depends on how much of the Target interface the
> adaptee can support.

The honest answer is to throw with a message that says why. The tempting one is to make
`remove` do nothing, so callers get a collection they believe they have modified. Note also
that exhaustion throws `NoSuchElementException` and not the adaptee's `IllegalStateException`:
**the target's contract wins**, which is half of what an adapter is for.

## 3 · `telemetry`

A Fahrenheit probe and a kelvin thermometer — different names, different units, no shared
supertype. The obvious design is one adapter class each, and another next quarter.

`PluggableSensorAdapter` takes the narrow interface as two functions instead, so **one class
adapts every instrument** that exists or ever will, and a third is two lambdas rather than a
new class.

The trade is real: nothing names the adaptee any more, so the compiler cannot check that the
right conversion was paired with the right instrument.

## Run it with

```bash
mvn -o test -Dtest='LegacyGatewayAdapterTest,CursorIteratorAdapterTest,PluggableSensorAdapterTest'

java -cp target/classes dev.kaldiroglu.dp.structural.adapter.hw.payments.Main
java -cp target/classes dev.kaldiroglu.dp.structural.adapter.hw.iteration.Main
java -cp target/classes dev.kaldiroglu.dp.structural.adapter.hw.telemetry.Main
```
