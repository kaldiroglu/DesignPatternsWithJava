# Template Method — Application and Document

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

GoF's example in a short form. `Application.openDocument()` is the template method: it
checks the file with `canOpenDocument`, creates the document with `createDocument`, and adds
it with `addDocument`. `MyApplication` writes the two abstract steps.

The `gof` package has the full version from the book: `openDocument` is `final`, it also
opens and reads the document, and it has the hook `aboutToOpenDocument`.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.templateMethod.pattern.Test
```
