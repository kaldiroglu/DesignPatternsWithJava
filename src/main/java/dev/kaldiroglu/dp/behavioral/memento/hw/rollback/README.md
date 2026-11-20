# Memento — homework 2: roll back a batch of transfers

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: three transfers, and the second fails. How do you put every account back?

`Batch` takes a memento of every account before the first transfer. If any transfer
fails, it gives every account its memento back. The batch does not need to know how to
reverse a transfer.

| Class | Role |
|---|---|
| `Account` | Originator: `withdraw`, `deposit`, `save()` and `restore(saved)` |
| `Account.Saved` | Memento: the balance, readable only by `Account` |
| `Batch` | Caretaker: runs the transfers as one unit, and rolls back when one fails |
| `Batch.Transfer` | One transfer: from, to, amount |
| `Main` | Runs a batch that fails and a batch that succeeds |

## Diagram

![Memento - Rollback (Homework) - Class Diagram](uml/Memento%20-%20Rollback%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`BatchTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=BatchTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.hw.rollback.Main
```
