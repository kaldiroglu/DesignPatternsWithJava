# Strategy — GoF's own example

*Claude Opus 5 (claude-opus-5) — Created on 2026-08-19*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 315-316. A document editor has to break a stream of text into lines,
and there is more than one algorithm for doing it.

## Before the pattern — `problem.Composition`

A document that also typesets. GoF give three reasons not to write it this way, and all
three are visible in the file:

- *"Clients get more complex if they include the line breaking code."* Read how much of the
  class is about laying out lines and how little is about being a document.
- *"Different algorithms will be appropriate at different times."* That sentence is the
  `quality` boolean, and it is a branch every future algorithm must be threaded through.
- *"It's difficult to add new algorithms or vary existing ones."* A third algorithm is a
  third branch in a method that already works for two — and the greedy pass is written
  twice, because the global algorithm needs it as a subroutine and there is nowhere to put
  it.

## After it — `solution`

`Compositor` is the Strategy: one method, handed the components and the measure, answering
where the lines break. `Composition` is the Context, and GoF's own name for it; it holds a
`Compositor` and forwards to it.

| Class | GoF | What it does |
|---|---|---|
| `SimpleCompositor` | p. 316 | Fills each line until the next component will not fit. |
| `TeXCompositor` | p. 316 | Reads the whole paragraph, then lays it out to the narrowest measure that still fits in the same number of lines. |
| `ArrayCompositor` | p. 316 | A fixed number of components per row, for breaking icons into rows. |

**`ArrayCompositor` is the one to stop on.** It ignores the measure entirely — six to a row
whatever they are, coming out 33 wide in a 26-column measure, which neither text algorithm
would ever produce. An interface designed around "fit text to a width" could not have held
it, and the fact that it fits is the evidence that `Compositor` describes an *algorithm*
rather than a variation on one.

## The figures

Both designs lay out the same paragraph — GoF's own motivation sentence, in a 26-column
measure — and `CompositorTest` asserts every number:

| Claim | Figure |
|---|---|
| Greedy filling, four lines | worst gap **8** |
| Reading the paragraph first, four lines | worst gap **5** |
| `ArrayCompositor(6)`, first row | **33** wide, in a 26-column measure |

The width matters. At most measures the two text algorithms agree, and 26 is one where they
do not — a demonstration that only works on one input is worth knowing about before it is
put on a slide.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest='CompositorTest'
```
