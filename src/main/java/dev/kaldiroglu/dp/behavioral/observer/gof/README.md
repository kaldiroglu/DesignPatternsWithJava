# Observer — GoF's own example

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 293–303. A timer keeps the time, and clocks show it.

## Before the pattern — `problem.ClockTimer`

The timer draws both clocks itself on every tick. A third clock is an edit to the timer.
`problem.Main` prints two ticks of both clocks.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `Subject` | Subject | Keeps the observers; `attach`, `detach`, `notifyObservers` |
| `Observer` | Observer | `update(Subject changed)` |
| `ClockTimer` | ConcreteSubject | Ticks and notifies; knows nothing about clocks |
| `DigitalClock`, `AnalogClock` | ConcreteObserver | Pull the time from the timer; detach when closed |

The clocks pull the time after they are told something changed: the pull model.
`solution.Main` attaches two clocks, closes the analog clock after two ticks, and ticks once
more: the third tick draws only the digital clock.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.gof.problem.Main
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.gof.solution.Main

# the tests of this example
mvn -o -q test -Dtest='dev.kaldiroglu.dp.behavioral.observer.gof.**'
```
