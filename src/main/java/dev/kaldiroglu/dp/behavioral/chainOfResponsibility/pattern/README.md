# Chain of Responsibility — help topics from specific to general

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Three handlers give help for three contexts: more specific, specific and generic. A
handler that matches the context returns its help. A handler that does not match asks the
next handler, then attaches its own help to the answer, so more than one handler takes
part in one request.

| Class | Role |
|---|---|
| `Handler`, `AbstractHandler` | Handler — `handleRequest(Context)` |
| `ConcreteHandler1`, `2`, `3` | ConcreteHandler |
| `Help`, `Help1`, `Help2`, `Help3` | The answer, which can hold other help |
| `Context` | `GENERIC`, `SPECIFIC`, `MORE_SPECIFIC` |
| `Test` | Client |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern.Test
```
