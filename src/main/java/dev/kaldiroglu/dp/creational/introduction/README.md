# Introduction to Creational Patterns

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-01*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The examples for the chapter before the five creational patterns. They show why creating an
object is harder than using it. No pattern is taught here.

| Package | What it shows |
|---|---|
| `employee.problem` | 21 fields: setters leave fields empty, and swapped arguments compile |
| `employee.better` | The same employee made of five smaller objects |
| `db.problem` | A connection with setters accepts a missing database |
| `db.better` | The same connection refuses to be created without one |
| `bank` | One invariant: setters, a constructor that checks, an object that calculates, a record |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o test -Dtest='dev.kaldiroglu.dp.creational.**'
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.creational.introduction.bank.SecureLoanMain
```
