# Memento — GoF's constraint solver

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 283–291. In a graphics editor, a line connects two boxes, and a
constraint solver keeps the line attached when a box moves. Here the line has one bend; the
solver keeps the bend where it is when it can, and moves it to the middle when a box passes
it. So the line depends on the moves that came before.

Box A is at 0, box B at 100, the bend at 50. B moves 60 to the left, past the bend, and the
bend goes to 20. Then the move is undone.

## Before the pattern — `problem`

`MoveCommand.unexecute()` moves B back and solves again. B returns to 100, but the bend
stays at 20, because 20 still lies between the boxes. To restore the bend, the command would
need the solver's private state.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `ConstraintSolver` | Originator | `createMemento()`, `setMemento(Memento)` |
| `ConstraintSolver.Memento` | Memento | The bend, in a private field with no getter |
| `MoveCommand` | Caretaker | Takes a memento before the move; gives it back on undo |

After undo the bend is at 50 again.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.gof.Main
```
