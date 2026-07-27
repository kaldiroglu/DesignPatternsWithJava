<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `decorator.middleware` — five cross-cutting concerns around one remote call

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

An order system asks a supplier for a price. Over eighteen months, five requirements arrive
around that one call: log it, time it, retry it, cache it, and stay inside a contractual
quota. This package contains **three naive designs and three Decorator designs**, all
runnable, all tested, all measured against the same scenario.

## Layout

```
middleware/
├── uml/                  5 diagrams, .puml + .png
├── domain/               PriceFeed, Quote, Clock/ManualClock, CallLog, Metrics,
│                         SimulatedRemotePriceFeed, VendorPriceFeed (final), exceptions
├── problem/              CopyPasteOrderService, FlaggedPriceFeed, 5-class subclass chain
└── solution/
    ├── classic/          PriceFeedDecorator + 5 decorators
    ├── functional/       PriceFeedMiddleware — the same concerns as lambdas
    └── fluent/           PriceFeedPipeline — assembly in reading order
```

## The measuring instruments

Nothing here is asserted where it can be measured:

- **`SimulatedRemotePriceFeed` counts its calls.** Caching means fewer, retrying means
  more, and ordering changes the number. Failures are *scripted* (`failNext(n)`), never
  random, so every run produces the same output.
- **`ManualClock` only moves when told.** Latency, TTL expiry and rate-limit windows are
  exact. **No test sleeps.**
- **`CallLog` and `Metrics` collect into lists**, so "what was logged, and in what order" is
  something a test can assert — which is how the effect of ordering becomes visible.

## The three naive designs (`problem`)

| Design | It works, but |
|---|---|
| `CopyPasteOrderService` | Two call sites that were one method. Four defects have already drifted in: 3 retries against 2, a missing failure-log line, a cache timestamp that is never written (so **the cache on that path never works at all** — every entry it writes is unreadable), and `<` become `<=`. |
| `FlaggedPriceFeed` | Five booleans, **thirteen constructor parameters**, 32 configurations in one method. Every new concern edits it. The cache/retry order is welded in. No concern can be tested alone, and switching caching on silently redefines what the timing metric measures. |
| `CachingRetryingLoggingPriceFeed` | Concerns get their own classes — joined by inheritance. Logging must be copied; the class name has to state both its contents and their order; five concerns in every order would take **325 classes**; and it cannot touch `VendorPriceFeed`, which is `final`. |

## The Decorator designs (`solution`)

**`classic`** — six classes, every concern, every order. Note how often each forwards —
GoF's "*may optionally* perform additional operations" (p. 178) is doing real work:

| Decorator | Forwards |
|---|---|
| Logging, Timing | exactly once |
| Retrying | **one or more** times |
| Caching | **zero or one** — nothing below it runs on a hit |

**`functional`** — the same concerns as `UnaryOperator<PriceFeed>` lambdas, folded so the
first listed is outermost. `PriceFeed` has one method, so in Java a lambda already *is* one.

**`fluent`** — `PriceFeedPipeline.around(feed).withTiming(...).withRetry(3).build()`, first
listed = outermost, so the code reads in the direction a request travels.

`VariationsTest` asserts all three build behaviorally identical chains.

## Ordering — the centerpiece

| Chains | Supplier calls | Difference |
|---|---|---|
| `Logging(Retrying(f))` against `Retrying(Logging(f))` | 2 and 2 | **2 log lines against 4** — the failure is invisible in one |
| `Timing(Caching(f))` against `Caching(Timing(f))` | 1 and 1 | **2 samples against 1** — "caller's wait" against "supplier's latency" |
| `RateLimit(Cache(f))` against `Cache(RateLimit(f))` | 1 and 1 | **3 quota against 1** — the first burns a real quota three times faster |
| `Caching(Retrying(f))` against `Retrying(Caching(f))` | 2 and 2 | **no difference** — failures are not cached, so reason per pair |

## What the tests establish — 31 tests

| Test | Count | Point |
|---|---|---|
| `ProblemTest` | 9 | All three naive designs work — and each is measured for what it costs |
| `ClassicDecoratorTest` | 12 | Each decorator alone; retry respects retryability; failures are not cached; decoration of a `final` class; identity is not preserved |
| `OrderingTest` | 4 | The four ordering results above |
| `VariationsTest` | 4 | Classic, functional and fluent are behaviorally identical |
| `DesignComparisonTest` | 2 | All designs produce the same quote from the same supplier calls |

## Run it with

```bash
mvn -o test -Dtest='ProblemTest,ClassicDecoratorTest,OrderingTest,VariationsTest,DesignComparisonTest'
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.middleware.Main
plantuml -tpng uml/**/*.puml
```
