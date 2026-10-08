# Observer — a stock price and its readers

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Observer deck. A price feed has three readers: a chart, a
ticker and an alert that must fire whenever the price reaches 105.

## Three attempts — `problem`

| Stage | Class | What it gets right | What it costs |
|---|---|---|---|
| one | `DirectPriceFeed` | No change is missed | The feed knows every reader by class |
| two | `PollingReaders` | The feed knows nobody | Readers are late, and most reads find nothing |
| three | `ChangeOnlyReaders` | Polls often, works only on a change | A change undone between two polls is never seen |

**Where stage three fails.** In ten seconds of polling, ten times a second, the readers make
100 reads. The price goes 100 → 102 → 106 → 100 → 103, and the step to 106 and back happens
between two polls. The chart shows 102, 100, 103, and the alert at 105 never fires.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `PriceFeed` | Subject | Keeps its listeners and tells them at the moment the price changes |
| `PriceListener` | Observer | One method, so a lambda can be a listener |
| `PriceChange` | the event | Symbol, old price, new price |
| `Chart`, `Ticker`, `PriceAlert` | ConcreteObserver | Each reacts in its own way |
| `Main` | — | Runs the same prices through stage three and through listeners |

With listeners the chart shows 102, 106, 100, 103, and the alert fires.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.solution.Main
```
