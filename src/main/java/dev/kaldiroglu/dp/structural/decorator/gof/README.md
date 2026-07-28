<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `decorator.gof` — the two examples from the book

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Decorator,
pp. 175–184.

Both examples the chapter gives, each implemented twice: once by **subclassing**
(`problem`) and once by **decorating** (`solution`). A comparison test proves the two
produce identical output, so every remaining difference is a difference of design.

## Layout

```
gof/
├── uml/                  6 diagrams, .puml + .png
├── visual/               Canvas, TextLayout, Main
│   ├── problem/          TextView + 4 subclasses
│   └── solution/         VisualComponent, TextView, Decorator, Border, Scroll
└── stream/               Stream, Codecs, Main
    ├── problem/          FileStream, SocketStream + 4 subclasses
    └── solution/         FileStream, SocketStream, StreamDecorator, Compressing, ASCII7
```

## Example 1 — VisualComponent (Motivation, p. 175)

**`problem`** — five classes for two embellishments. `drawScrollbar` appears **three
times**, because no class can inherit from both parents, and the same two embellishments
in the other order draw a different picture, so that needs its own class too.

**`solution`** — four classes covering every combination.

Both render onto a character `Canvas`, so the tests assert the actual picture:

```
Border(Scroll(text))    Scroll(Border(text))    Border(Border(text))

+------+                +-----+^                +-------+
|hello^|                |hello|#                |+-----+|
|therev|                |there|#                ||hello||
+------+                +-----+v                ||there||
                                                |+-----+|
                                                +-------+
```

The third is GoF's Consequence 1 — "decorators make it easy to add a property twice"
(p. 178) — and it costs nothing.

## Example 2 — Stream (Sample Code, pp. 182–184)

GoF's `HandleBufferFull` mechanic is kept rather than simplified away: it is what makes
these decorators interesting, because they transform the **buffer** on its way out rather
than merely observing the call. GoF's own client line works verbatim:

```java
Stream aStream = new CompressingStream(new ASCII7Stream(new FileStream()));
aStream.putInt(12);
aStream.putString(" aaa café");
aStream.close();                       // -> "12 3a cafe"
```

This example carries an ordering lesson the visual one cannot. Folding `ä` to `a` next to
an existing `a` creates a run that compression can only exploit if it runs *after* the
folding:

| Chain | Input `"aä"` | Output |
|---|---|---|
| `Compressing(ASCII7(file))` | compress first — two different characters | `aa` |
| `ASCII7(Compressing(file))` | fold first — compression finds a run | `2a` |

## What the tests establish — 22 tests

| Test | Count | Point |
|---|---|---|

## Run it with

```bash
mvn -o test -Dtest='VisualProblemTest,VisualSolutionTest,DesignComparisonTest,StreamTest'

java -cp target/classes dev.kaldiroglu.dp.structural.decorator.gof.visual.Main
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.gof.stream.Main

plantuml -tpng uml/**/*.puml
```
