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
would need. The deck uses that as an exercise. `AirConditioner.turnOff()` keeps the room's
temperature, so turning it back on at the same temperature starts only the fan.

## The lambda version — `lambda`

`lambda.ACSwitch` holds each request as a method reference to the air conditioner —
`ac::turnOn`, `ac::turnOff`, `ac::turnOnHeater`, `ac::turnOnCooler` — as a `Consumer` of a
temperature or a `Runnable`. The four command classes are gone. A lambda cannot implement
`Command`, which has three methods, so the lambda switch has no `undo` and `redo`; in this
example they are empty anyway. `lambda.Main` runs the same steps as `Person` and prints the
same lines. A request that must be undone still needs a class.

## Tests

`src/test/java/dev/kaldiroglu/dp/behavioral/command/ac/AirConditionerTest.java` holds 11
tests. They check what the switch and the air conditioner print: the fan, the heater or
the cooler when it is turned on; the warnings when it is already on or off, or off when the
heater or cooler is asked for; that the cooler only cools and the heater only heats; that
each command passes its request to the air conditioner; that the switch holds four
`Command` fields; that turning off keeps the room temperature; and that `undo` and `redo`
do nothing yet.

`ac/lambda/LambdaSwitchTest.java` adds 3 tests for the lambda switch.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.ac.Person
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.ac.lambda.Main

# the tests
mvn -o -q test -Dtest='AirConditionerTest,LambdaSwitchTest'
```
