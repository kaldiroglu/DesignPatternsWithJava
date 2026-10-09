# Chain of Responsibility — GoF's context-sensitive help

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 223–232. The user presses F1 on a control. If the control has its own
help, it shows it; if not, its container answers, and in the end the application does.

## Before the pattern — `problem.HelpDesk`

One class knows every control by name and which dialog it sits in.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `HelpHandler` | Handler | Has a topic or not, and a successor |
| `Widget` | — | A control; its successor is usually its parent |
| `Button`, `Dialog` | ConcreteHandler | A dialog's successor is the application |
| `Application` | ConcreteHandler | The end of every chain |

The chain is not a separate list of links: it is the window's own containment.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.Main
```
