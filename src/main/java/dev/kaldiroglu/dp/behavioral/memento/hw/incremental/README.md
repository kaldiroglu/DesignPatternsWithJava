# Memento — homework 3: a memento of only what changed

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: a sheet has ten thousand cells, and an edit changes two. What should one
memento hold?

Only the old values of the cells the edit changed. `Sheet.set` returns a `Change` with
those values, and `Sheet.undo(change)` puts them back. This is GoF implementation issue 2
(storing incremental changes).

| Class | Role |
|---|---|
| `Sheet` | Originator: the cells; `set(newValues)` returns a `Change`, `undo(change)` takes it back |
| `Sheet.Change` | Memento: the old values of the changed cells only |
| `Main` | Caretaker: edits two cells of a 100 by 100 sheet and undoes the edit |

## Diagram

![Memento - Incremental Changes (Homework) - Class Diagram](uml/Memento%20-%20Incremental%20Changes%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`SheetTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=SheetTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.hw.incremental.Main
```
