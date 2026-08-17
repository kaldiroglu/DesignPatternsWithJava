<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.shape` — shapes on devices, and the sentence that decides everything

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

A drawing editor with three shapes and two devices. The classic Bridge domain, and the one
where the pattern is most often got subtly wrong.

## The mistake this package now exists to prevent

An earlier version of `ShapeDrawer` declared `drawCircle`, `eraseCircle`, `drawRectangle`,
`eraseRectangle` — **one method per shape**. It looks harmless. It is not: adding a
`Triangle` then forces *every* drawer to grow a `drawTriangle`, and the two hierarchies are
no longer independent, which is the only thing Bridge exists to buy.

GoF settle it on p. 154:

> "The Implementor interface doesn't have to correspond exactly to Abstraction's interface;
> in fact the two interfaces can be quite different. Typically the Implementor interface
> provides only primitive operations, and Abstraction defines higher-level operations based
> on these primitives."

So `ShapeDrawer` now offers three primitives — `drawLine`, `drawArc`, `clear` — and each
shape composes them:

| Shape | Composed from |
|---|---|
| `Circle` | one arc |
| `Rectangle` | four lines |
| `Triangle` | three lines |

has a test that fails the day a shape name appears in the implementor interface again.

## The detail worth stopping on

MacOS draws arcs natively. XWindows has no arc call, so `XWindowsDrawer.drawArc` builds one
out of **sixteen** line segments — GoF's Presentation Manager detail (p. 157) in a second
domain. The same `Circle` object produces:

```
MacOS     1 device call   (arc r=20 from 0 sweep 360)
XWindows  16 device calls (line, line, line, ...)
```

The circle never learns why, and must not have to.

## The two designs, counted

| | `problem` | `pattern` |
|---|---|---|
| 3 shapes × 2 devices | 6 leaf classes + 4 abstract = **10** | 3 shapes + 2 drawers + 2 bases = **7** |
| A third device | +3 leaf classes | **+1 class** |
| A fourth shape | +2 leaf classes | **+1 class**, no drawer touched |
| Move a shape to another device at run time | impossible — the device *is* the class | `shape.setDrawer(other)` |

## Run it with

```bash
mvn -o test -Dtest=ShapeBridgeTest

java -cp target/classes dev.kaldiroglu.dp.structural.bridge.shape.solutionn.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.shape.problem.Main
```
