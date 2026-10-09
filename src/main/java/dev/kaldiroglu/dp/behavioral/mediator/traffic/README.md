# Mediator — a traffic police officer at a junction

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Cars approach a junction. They do not negotiate with each other: each one asks the traffic
police officer for permission to pass, and the officer lets one car through at a time.

| Class | Role |
|---|---|
| `TrafficMediator` | Mediator — `receive`, `askPermitToPass`, `done` |
| `TrafficPolice` | ConcreteMediator — keeps the junction busy or free |
| `Vehicle` | Colleague |
| `Car` | ConcreteColleague; each car is a `Thread` |
| `Junction` | The shared resource |
| `Test` | Client — five cars, each on its own thread |

Each car runs on its own thread, so the order of the output changes from run to run.

Because every car calls the officer from its own thread, `askPermitToPass` checks whether
the junction is busy and makes it busy inside one `synchronized` block. The car then
proceeds, or waits, outside the lock. A car that has to wait asks again from the loop in
`run()`.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.mediator.traffic.Test
```
