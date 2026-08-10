<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-08-10
-->

# `gof.visual.skinandguts` — GoF implementation issue 4, worked

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

*Design Patterns* asks, under Implementation issue 4 (p. 180), whether you are **changing
the skin of an object or changing its guts**. Decorator is the skin; Strategy is the guts.
The question is usually presented as a choice between two patterns. This package shows the
more useful case: both at once, on GoF's own border example, each solving the problem it is
good at.

## The two designs

Both wrap a `TextView` in a border. Both draw the same three pictures. They differ in one
thing only — how the decorator answers *which border?*

| Class | Role | How it decides |
|---|---|---|
| `SwitchingBorderDecorator` | Decorator | a branch on a closed `Style` enum |
| `StyledBorderDecorator` | Decorator **and** Strategy's context | it asks a `BorderStyle` |
| `BorderStyle` | **Strategy** | `stroke(canvas, x, y, width, height)` |
| `SolidBorder` · `DashedBorder` · `ThickBorder` | ConcreteStrategy | one class per style |

```java
// the branch
if (style == Style.SOLID)       { ... }
else if (style == Style.DASHED) { ... }
else if (style == Style.THICK)  { ... }

// the hook
style.stroke(canvas, x, y, width(), height());
```

## Why both patterns are here

They are not alternatives. They answer different questions:

- **Decorator adds a responsibility to an object that was never designed for it.** `TextView`
  is wrapped and knows nothing about borders. A test asserts it names no type from this
  package in any field or signature.
- **Strategy varies one decision inside an object that was designed for it.**
  `StyledBorderDecorator` has a hook, and holds the object that fills it.

The rule of thumb follows: reach for a decorator when the object must not know, and a
strategy when it is yours to design and the variation is one decision inside it. GoF add
that Strategy is the better answer *"where the Component class is intrinsically
heavyweight"*, because a decorator pays for the whole Component interface while a strategy
pays only for the hook.

## What a fourth style costs

That is the whole argument, and the test measures it rather than asserting it in prose.
`aNewStyleNeedsNoEditToTheDecorator` writes a dotted border **inside the test file**, using
nothing but the `BorderStyle` interface, and renders it — without one line changing in the
main source tree. The switching design has no equivalent: its vocabulary is closed at three,
and a fourth means editing the enum plus the branch that reads it, then retesting the three
styles that were already working.

A second test makes the coupling explicit by reflection: `StyledBorderDecorator` references
`BorderStyle` and **no** concrete style; `SwitchingBorderDecorator` is coupled to its own
enum.

## The pictures

Asserted character for character by `theStylesDiffer`:

```
+-----+      +- - -+      #######
|hello|      |hello|      #hello#
+-----+      +- - -+      #######
 solid        dashed       thick
```

## Diagram

`uml/Decorator-Visual-Skin-And-Guts.png` — the shared skin on the left, and the two ways of
answering *which border?* on the right.

## Run it with

```bash
mvn -o test -Dtest=SkinAndGutsTest        # 5 tests
cd src/main/java/dev/kaldiroglu/dp/structural/decorator/gof/visual/skinandguts \
  && plantuml -tpng uml/*.puml            # regenerate the diagram
```
