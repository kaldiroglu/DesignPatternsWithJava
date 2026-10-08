# Command — the air conditioner switch

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-02*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A wall switch with four buttons — on, off, heat, cool — drives an
air conditioner through four commands.

| Class | Participant |
|---|---|
| `Person` | Client |
| `ACSwitch` | Invoker — holds four commands, one per button |
| `Command` | Command — `execute(Temperature)`, `undo()`, `redo()` |
| `TurnOnCommand`, `TurnOffCommand`, `HeatCommand`, `CoolCommand` | ConcreteCommand |
| `AirConditioner` | Receiver |
| `Temperature` | the argument |

**Left open on purpose.** `undo()` and `redo()` are declared and not yet implemented, and
each command holds only the air conditioner — not the temperature it replaced, which undo
would need. The deck uses that as an exercise. Note also that `AirConditioner.turnOff()`
resets the current temperature to zero, so turning it back on at 20 starts the heater.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.ac.Person
```
