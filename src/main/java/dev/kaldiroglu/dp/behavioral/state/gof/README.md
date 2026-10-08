# State — GoF's own example

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 305–313. A TCP connection is closed, listening or established, and it
answers each request differently in each state.

## Before the pattern — `problem.TCPConnection`

Every operation is a switch over the three states.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `TCPConnection` | Context | Forwards every request to its state; `changeState` is package-private |
| `TCPState` | State | An abstract class: every request is ignored by default |
| `TCPClosed`, `TCPListen`, `TCPEstablished` | ConcreteState | Each overrides what it answers and moves to the next state. Each is a single shared object. |

The `pattern` package holds an earlier outline of the same example, with empty methods.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.gof.Main
```
