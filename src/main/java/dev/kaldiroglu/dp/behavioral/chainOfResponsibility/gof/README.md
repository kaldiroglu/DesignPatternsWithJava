# Chain of Responsibility — GoF's context-sensitive help

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 223–232. The user presses F1 on a control. If the control has its own
help, it shows it; if not, its container answers, and in the end the application does.

## Before the pattern — `problem.HelpDesk`

One class knows every control by name and which dialog it sits in.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `HelpHandler` | Handler | Has a topic or not, and a successor |
| `Widget` | — | A control; its successor is usually its parent |
| `Button`, `Dialog` | ConcreteHandler | A dialog's successor is the application |
| `Application` | ConcreteHandler | The end of every chain |

The successor is a field of its own in `HelpHandler`, as in GoF's sample code. GoF's
`Widget` keeps a second field, `_parent`, that points to the same object.

## A variation — `parent`

GoF implementation issue 1 (implementing the successor chain) gives two choices: define
new links, or use existing links. `solution` defines a new link. `parent` uses an existing
one.

`parent.Widget` keeps its parent, the widget that holds it. Two methods follow that one
field: `path()` says where the widget is, and `handleHelp()` passes the request on. No
field is added for the chain. The three buttons get the same help as in `solution`, and
`ParentLinkTest` checks that, and that `parent` is the only field that holds a widget.

Each package has one runnable class, `Main`. The first three controls get the same help in
both, and the tests check that.

## Diagrams

`uml/` holds the class diagram, and a sequence diagram of the font button: it has no help
topic, its dialog has none either, so the request goes up to the editor, which answers.
`uml/Chain of Responsibility - Help (Parent) - Class Diagram` shows the `parent` variation.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile

# before the pattern: the help desk answers four controls
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.problem.Main

# with the chain: four controls, the last with no help anywhere
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Main

# existing links: each button's path, then its help
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.parent.Main

# the tests of this example
mvn -o -q test -Dtest='dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.**'
```
