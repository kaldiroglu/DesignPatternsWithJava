# Mediator — a traffic police officer at a junction

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Cars approach a junction. They do not negotiate with each other: each one asks the traffic
police officer for permission to pass, and the officer lets one car through at a time.

| Class | Role |
|---|---|
| `TrafficMediator` | Mediator — `askPermitToPass`, `done` |
| `TrafficPolice` | ConcreteMediator — keeps the junction busy or free, and the waiting cars in order |
| `Vehicle` | Colleague |
| `Car` | ConcreteColleague; a `Runnable`, and each car runs on its own thread |
| `Junction` | The shared resource |
| `Main` | Client — five cars, each on its own thread |

Each car runs on its own thread, so the order of the output changes from run to run.

A car asks once. If the junction is free, the officer tells it to proceed. If not, the
officer tells it to stop and adds it to the `waiting` queue, and the car blocks. When a car
is `done`, the officer tells the first waiting car to proceed. So no car polls, cars pass in
the order they asked, and the junction stays busy while it passes from one car to the next.

Because every car calls the officer from its own thread, the officer makes these decisions
inside one `synchronized` block, and calls the cars outside it.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.mediator.traffic.Main
```
