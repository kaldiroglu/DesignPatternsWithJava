# Mediator — GoF's font dialog

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 273–282. A font dialog has a list of fonts, a text field and an OK
button. Selecting a font puts its name in the field; OK is enabled only while the field has
text.

## Before the pattern — `problem.FontDialog`

The list box knows the entry field, the entry field knows the OK button, and the button
knows the dialog. The widgets work only in this dialog.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `DialogDirector` | Mediator | `widgetChanged(Widget)` |
| `FontDialogDirector` | ConcreteMediator | Creates the widgets; the dialog's whole behavior is in one method |
| `Widget` | Colleague | Knows its director; `changed()` |
| `ListBox`, `EntryField`, `Button` | ConcreteColleague | General widgets that know no other widget |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.mediator.gof.Main
```
