# Command — GoF's own example

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-02*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 233–242. A user-interface toolkit has menus, and a menu item has to do
something when it is clicked — but only the application that uses the toolkit knows what.

## The shared types

`Application`, `Document` and `Clipboard` are the application's side: the **Receivers**.
They know how to open, copy and paste, and nothing about menus.

## Before the pattern — `problem.MenuItem`

A menu item that knows what it does. It imports `Application` and `Document`, branches on
its own label, and has to be edited for every menu entry any application will add. The
label is a string, so `"paste"` instead of `"Paste"` compiles and fails on the first click.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `Command` | Command | `execute()` — one method, no arguments |
| `MenuItem` | Invoker | Holds a command and executes it when clicked; imports nothing from the application |
| `Menu` | — | A list of menu items |
| `PasteCommand` | ConcreteCommand | Forwards to `Document.paste()` |
| `OpenCommand` | ConcreteCommand | Asks for a name, creates a document, adds it, opens it |
| `MacroCommand` | ConcreteCommand | A sequence of commands, executed in order |
| `SimpleCommand<R>` | ConcreteCommand | GoF's C++ template: a receiver and an action — `new SimpleCommand<>(document, Document::paste)` |

`SimpleCommand` is where the language already is the pattern: the member-function pointer
GoF needed a template for is a method reference in Java. It suits commands that take no
arguments and cannot be undone; a command that has to remember something needs a class.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
```
