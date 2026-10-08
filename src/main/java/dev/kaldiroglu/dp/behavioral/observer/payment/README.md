# Observer — an invoice with java.util.Observable

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

An invoice tells the boss and the accountant when a payment is made. It uses the JDK's own
`java.util.Observable` and `java.util.Observer`, so it needs very little code: `Invoice`
extends `Observable`, calls `setChanged()` and `notifyObservers()`, and the observers
implement `update`.

Both types are deprecated since Java 9. The JDK's own note says the event model "is quite
limited, the order of notifications delivered by `Observable` is unspecified, and state
changes are not in one-for-one correspondence with notifications", and points to
`java.beans` and `java.util.concurrent.Flow` instead.

The order is visible here: the boss is added first, but the accountant is told first.
`Observable` is also a class, so `Invoice` cannot extend anything else.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.payment.Test
```
