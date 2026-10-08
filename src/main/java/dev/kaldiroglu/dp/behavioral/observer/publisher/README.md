# Observer — magazines and subscribers

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A publisher prints magazines, and people and institutions subscribe to them. When a new
issue comes out, every subscriber receives it and reacts in its own way.

| Class | Role |
|---|---|
| `Publication`, `AbstractPublication` | Subject — add, remove, publish |
| `Newsweek`, `FourFourTwo` | ConcreteSubject |
| `Subscriber`, `AbstractSubscriber` | Observer — `receive(Publication)` |
| `IndividualSubscriber` | ConcreteObserver — reads the issue |
| `InstitutionalSubscriber` | ConcreteObserver — puts it on the shelf |
| `Publisher`, `Test` | Client |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.observer.publisher.Test
```
