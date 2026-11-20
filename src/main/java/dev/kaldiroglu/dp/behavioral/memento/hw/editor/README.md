# Memento — homework 1: undo and redo in a text editor

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: how do you use the Memento pattern for undo and redo? The answer here is
two stacks of mementos in a caretaker.

- Before every change, `History` saves the current state on the undo stack, and a new
  change clears the redo stack.
- Undo saves the current state on the redo stack, then restores the top of the undo stack.
  Redo does the opposite.
- The memento holds the text and the cursor, so undo also puts the cursor back.

| Class | Role |
|---|---|
| `TextEditor` | Originator: a text and a cursor; `save()` and `restore(snapshot)` |
| `TextEditor.Snapshot` | Memento: private fields, a private constructor, no getters |
| `History` | Caretaker: the undo and redo stacks |
| `Main` | Types two words, undoes twice and redoes once |

## Diagram

![Memento - Text Editor (Homework) - Class Diagram](uml/Memento%20-%20Text%20Editor%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`HistoryTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=HistoryTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.hw.editor.Main
```
