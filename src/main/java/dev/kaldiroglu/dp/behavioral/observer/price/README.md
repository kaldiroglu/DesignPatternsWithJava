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

Each slide has its own runnable class, so a run prints only what that slide shows:

| Class | Slide | What it prints |
|---|---|---|
| `StageOneMain` | stage one | The screens after each of four prices; the alert fires on 106 |
| `StageTwoMain` | stage two | The reader thread polls three times in 2.5 seconds: every screen after each poll, the same price twice, then the change |
| `StageThreeMain` | stage three | The reader thread polls ten times a second: every screen after each of two changes, and only the changes reach the chart |
| `FastSpikeMain` | the problem with stage three | Every screen after each change it saw: three changes, no alert, then 100 reads |

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `PriceFeed` | Subject | Keeps its listeners and tells them at the moment the price changes |
| `PriceListener` | Observer | One method, so a lambda can be a listener |
| `PriceChange` | the event | Symbol, old price, new price |
| `Chart`, `Ticker`, `PriceAlert` | ConcreteObserver | Each reacts in its own way |
| `ListenersMain` | — | Runs the prices through the listeners, one of them a lambda |
| `TwoWaysMain` | — | Runs the same prices through stage three and through listeners |
| `TenSeconds` | — | The ten seconds of prices both runnable classes use |

With listeners the chart shows 102, 106, 100, 103, and the alert fires.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile

# the three attempts, one slide at a time
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.problem.StageOneMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.problem.StageTwoMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.problem.StageThreeMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.problem.FastSpikeMain

# the pattern
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.solution.ListenersMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.price.solution.TwoWaysMain

# the tests of this example
mvn -o -q test -Dtest='dev.kaldiroglu.dp.behavioral.observer.price.**'
```
