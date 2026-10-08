# Template Method — a repeated task

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A general algorithm for a task that repeats: prepare, then before / the task / after as
many times as asked, waiting between repetitions, then clean up.

| Class | Role |
|---|---|
| `Task` | AbstractClass. `run()` is the template method and is `final`. `doTask()` is abstract; `prepare`, `before`, `after` and `clean` are hooks with a default. |
| `Fax`, `Scan` | ConcreteClass — write `doTask()` only |
| `Print` | ConcreteClass — also overrides the `prepare` and `clean` hooks |
| `Test` | Client, with a `main` method |

`Test` runs a print task ten times with a one-second interval, so it takes about ten
seconds.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.templateMethod.task.Test
```
