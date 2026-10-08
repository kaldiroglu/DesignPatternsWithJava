# Template Method — GoF's own example

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 325–330. A framework has many applications, and opening a document is
the same steps in all of them: check the file, create the document, add it, open it, read
it.

## Before the pattern — `problem`

`DrawApplication` and `SpreadsheetApplication` each write the whole of `openDocument`. The
spreadsheet adds a step ("remember the last file") in its own copy, in a place it chose.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `Application` | AbstractClass | `openDocument` is the template method, and `final` |
| `Application.canOpenDocument` | primitive operation | must be written by a subclass |
| `Application.doCreateDocument` | factory method | creates the right kind of document |
| `Application.aboutToOpenDocument` | hook | does nothing by default |
| `DrawApplication`, `SpreadsheetApplication` | ConcreteClass | the spreadsheet uses the hook |
| `Document`, `DrawDocument`, `SpreadsheetDocument` | | `doRead` is a primitive operation |

The `pattern` package holds a shorter version of the same example.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.templateMethod.gof.Main
```
