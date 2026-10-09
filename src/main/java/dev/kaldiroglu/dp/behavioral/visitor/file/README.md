# Visitor — text files and XML files

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A text file must have its format checked, and an XML file must be validated, before either
is read.

| Package | Design |
|---|---|
| `domain` | `File`, `TextFile`, `XMLFile`: open, read, close |
| `problem1` | `checkFormat()` on `TextFile`, `validate()` on `XMLFile`; the client tests types |
| `problem2` | `FileOperator` does the type tests and the casts |
| `pattern1` | `File.accept(Visitor)`; `FileVisitor` has `visit(TextFile)` and `visit(XMLFile)` |

The checks pass four times in five, chosen with `Math.random()`, so the output changes
from run to run.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.file.pattern1.Test
```
