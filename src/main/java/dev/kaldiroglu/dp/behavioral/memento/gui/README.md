# Memento — a GUI component

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A window has a position and a size. Its state is a separate object, `GuiComponentState`,
and a memento keeps that object so the window can go back to it.

| Class | Role |
|---|---|
| `GuiComponent` | Originator — `createMemento()` and `restore(memento)` |
| `GuiComponentMemento` | Memento — holds one `GuiComponentState` |
| `GuiComponentState` | The state, as one record |
| `Main` | Caretaker — keeps the memento while the window moves and resizes, then gives it back |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.gui.Main
```
