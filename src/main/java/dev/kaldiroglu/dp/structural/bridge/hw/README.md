<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.hw` — worked solutions to the homework

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Three problems set at the end of the Bridge session, solved. **Try them before you read
them.** Each package has a runnable `Main` and a test class.

| Package | Problem | What the code settles |
|---|---|---|
| `statementrun` | Three documents, three media, one of them a screen reader | Five primitives, every one of which a voice can answer |
| `paymentdesk` | Four payment kinds over bank, wallet and cash | Cash collapses the two phases, and no caller branches |
| `routeplanner` | Three route kinds, then the vendor changes | A test that fails if a vendor type reaches the abstraction |

## 1 · `statementrun`

The screen reader decides the shape of `Medium`. The obvious first draft has `drawBox`,
`setFont`, `newPage` and `margin` on it — and every one of those is a question about
**paper**. A voice has none of them.

What survives is the set of primitives that describe **meaning** rather than ink: `heading`,
`field`, `row`, `total`. That is the test for whether something belongs on an Implementor,
and accessibility is the honest way to apply it, because the third medium is a real user
rather than a hypothetical platform.

A test asserts `Medium` declares exactly those five methods, and that the spoken output
contains no `<`, no `=` and no newline.

Three documents and three media: **6 classes, not 9.**

## 2 · `paymentdesk`

A bank gateway and a wallet authorize first and capture later. Cash does neither — the money
is in the drawer or it is not. There are three defensible answers, and the Javadoc on
`PaymentProvider` records why the first two were rejected:

1. **Widen** the interface with `supportsTwoPhase()`. Rejected: that is a boolean asking
   "which implementation are you?", and every branch on it is a piece of the abstraction
   that now knows about providers.
2. **Split** into two implementor interfaces and let the abstraction discover which it
   holds. Rejected for the same reason, wearing a type instead of a boolean.
3. **Keep the two-phase shape and let cash collapse it.** Taken here.
   `CashDrawer.authorize` returns an authorization already marked settled, and its capture
   is a no-op that hands back the receipt.

The cost is real and is stated: there is no `void` primitive, because only two of the three
providers could implement it. A test walks `PaymentProvider` and fails on any
boolean-returning method.

## 3 · `routeplanner`

The homework asks for a diff — *after the swap, what changed?* — and a diff cannot be
asserted. So the test asserts the property that makes an empty diff possible: it reflects
over every class on the abstraction side and fails if a concrete provider type appears in
any field or constructor. That test is worth stealing for real code, because it catches the
leak on the day it is introduced rather than on the day the vendor changes.

The two providers disagree on purpose. The vendor surveyed Üsküdar → Levent and found steps,
so `StepFreeRoute` picks a different journey — different answer, identical routing code.

## Run it with

```bash
mvn -o test -Dtest='StatementRunTest,PaymentDeskTest,RoutePlannerTest'    # 16 tests

java -cp target/classes dev.kaldiroglu.dp.structural.bridge.hw.statementrun.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.hw.routeplanner.Main
```
