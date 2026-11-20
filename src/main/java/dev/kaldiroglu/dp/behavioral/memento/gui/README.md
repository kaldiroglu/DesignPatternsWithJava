# Memento — a GUI component

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A window has a position and a size. Its state is a separate object, `GuiComponentState`,
and a memento keeps that object so the window can go back to it.

The memento, `GuiComponent.Memento`, is nested in the window. Its field and its constructor
are private, and it has no methods. So `Main` can keep it and pass it back, but cannot read
it, change it, or make one with other values. This is GoF's narrow interface for the
caretaker.

| Class | Role |
|---|---|
| `GuiComponent` | Originator — `createMemento()` and `restore(memento)` |
| `GuiComponent.Memento` | Memento — holds one `GuiComponentState`; only `GuiComponent` can make or read it |
| `GuiComponentState` | The state, as one record |
| `Main` | Caretaker — keeps the memento while the window moves and resizes, then gives it back |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.gui.Main
```
