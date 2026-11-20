# State — a door, and who changes its state

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A door is open or closed. Opening an open door, or closing a closed one, prints a message
and changes nothing. All three versions start closed and print the same story.

The door is too small to need the pattern: two states, and one `if` in each method, are clear
code. It is here to show the main design decision of the State pattern: who changes the state.

| Package | Who changes the state |
|---|---|
| `problem` | Nobody: a boolean and an `if` in each method |
| `pattern1` | The states. Each state knows the other one and tells the door to change. |
| `pattern2` | A central `DoorStateManager`. The door passes each request to its state, which does the work, then asks the manager `next(state, request)`. The manager's table maps a state and a request to the next state; the states know no other state. |

In `pattern1` the transitions are spread over the states. In `pattern2` they are in one table,
so a new state or a new request is new lines in the manager, and the existing states do not
change.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.door.pattern1.Main
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.door.pattern2.Main
```
