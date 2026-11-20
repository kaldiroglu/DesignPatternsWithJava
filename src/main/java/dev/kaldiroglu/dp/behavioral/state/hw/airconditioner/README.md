# State — homework 1: an air conditioner

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: an air conditioner is off, idle, cooling or heating. Where does the
"which state now?" rule live?

In the state base class, `AcState.running`. Every running state uses the same rule:
cooling if the room is warmer than the target, heating if colder, idle if equal. So it is
written once, and every running state calls it when the room or the target changes. When
the air conditioner is off, a new target is kept and nothing runs.

| Class | Role |
|---|---|
| `AirConditioner` | Context: the target, the room temperature, and the current state |
| `AcState` | State: the shared rule in `running`, and the defaults |
| `Off`, `Idle`, `Cooling`, `Heating` | Concrete states, each one shared object |
| `Main` | Turns it on in a warm room, lets the room cool, raises the target, turns it off |

## Diagram

![State - Air Conditioner (Homework) - Class Diagram](uml/State%20-%20Air%20Conditioner%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`AirConditionerTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=AirConditionerTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.hw.airconditioner.Main
```
