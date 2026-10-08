# State — a door, and who changes its state

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A door is open or closed. Opening an open door, or closing a closed one, does nothing.

| Package | Who changes the state |
|---|---|
| `problem` | Nobody: a boolean and an `if` in each method |
| `pattern1` | The states. Each state knows the other one and tells the door to change. |
| `pattern2` | A central `DoorStateManager`. The states ask the manager; only it knows all the states. |

This is the main design decision of the State pattern: are the transitions spread over the
states, or kept in one place?

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.door.pattern1.Test
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.door.pattern2.Test
```
