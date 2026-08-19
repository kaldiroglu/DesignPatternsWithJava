# CLAUDE.md

Guidance for Claude Code when working on **Design Patterns with Java**.

*Claude Opus 5 (claude-opus-5) — Created on 2026-08-19*

This is the **source of truth** for the design-patterns course. The C# port and both slide
decks follow what is here. The cross-repository rules — the push order, which repositories
have remotes, what "push all" means — are in `~/.claude/CLAUDE.md`, because they govern all
four at once and no project file is loaded from a sibling repository.

## What is here

One Maven module, `DesignPatternsWithJava`, Java 25, JUnit 6.0.3, Surefire 3.5.2.

Package root `dev.kaldiroglu.dp.<family>.<pattern>`:

| Family | Ported |
|---|---|
| `structural` | adapter, bridge, composite, decorator, facade, flyweight, proxy |
| `behavioral` | strategy |
| `creational` | the package exists and is empty |

Inside a pattern, one folder per worked example, and inside that the shape the decks quote:
`domain` for the shared types, `problem` (or a named naive stage) for the designs that fail,
`solution` or `pattern` for the answer, and `hw` at the pattern root for homework solutions.
`gof` holds the book's own example. Every worked example carries a `README.md` and a `uml/`
folder — 69 of them so far.

`tools/` is local only and never pushed.

## Commands

Everything is cached in `~/.m2`, so work offline:

```bash
mvn -o -q clean test                          # the whole suite
mvn -o test -Dtest='SolutionTest' -DfailIfNoTests=false
```

- **Read the per-suite lines, not just the exit code.** A single stale figure fails the
  build and says nothing about the other 480 tests.
- **Surefire never collects a class named `*Main`.** Renaming a test class to `Main`
  silently removes its tests from the run and the build still passes.

## Tests are the instrument, not regression cover

The decks quote numbers off these tests, so they are written to fail when the code changes:

- **Count the code; do not restate its numbers.** `assertEquals(9, 3 * 3)` proves something
  about integers and goes on passing the day a fourth channel is added and the slide still
  says nine. Count types out of the package instead — several suites walk the classpath
  directory to do it.
- **Every figure on a slide is asserted here.** If no test asserts it, it does not go on a
  slide. This is the rule that caught `GlyphFlyweightTest` still claiming 61 characters
  after the sample text had been changed to one that types 63.
- **A "this file contains no X" test must strip comments first.** Well-commented code names
  what it excludes, so a plain search matches the class's own javadoc and proves nothing.
- **Some numbers are properties of the input, not of the code.** The two text compositors
  agree at most measures and differ at 26 columns; the paragraph and the width were chosen
  so the example demonstrates something. Say so in the README when it happens.

## Diagrams

Each worked example has a `uml/` folder holding `.puml`, `.png` and `.svg`, rendered with
`plantuml -tpng` and `-tsvg`. These are written for reading and carry long notes; the deck
repositories keep their own slimmer versions for the projector.

- **A `<b>` span does not survive a line break.** PlantUML closes it at end of line and the
  stray `</b>` prints literally on the image. Keep each `<b>…</b>` on one line.
- **Graphviz places packages by content, not by declaration order.** A diagram of three
  numbered designs comes out 2, 1, 3. Hidden edges between the packages' members pin it.
- **Verify names against the source rather than recalling them.** A short script over the
  `.puml` files catches drift after a rename.

## Conventions

- No Turkish in code, comments or identifiers. American English throughout.
- Javadoc carries the teaching. A class in `problem` explains what it costs; a class in
  `solution` explains what changed. These comments are quoted on slides.
- Naive designs are written to be defensible. Each stage is a real improvement on the one
  before, and the code should read as something a careful team would ship.
- When two packages hold the same concept, they may share a class name — `problem.Window`
  and `solution.Window`. Java resolves a partially-qualified name from the root, not
  relative to the current package, so tests referring to both need fully-qualified names.
