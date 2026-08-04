<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `composite.graphic` — shapes on a canvas

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Four leaf shapes and a canvas that holds them — and holds other canvases, which is the whole
point. `shapeCount()` is the payoff: a leaf answers 1, a canvas adds up its children, and a
client asks one object for a number that covers the whole tree.

## The design decision

Child management is on **`CompositeGraphic`**, not on `Graphic`. That is the **safe** side
of GoF's implementation issue 4 (p. 167), and the opposite of what the book recommends:

- **The gain.** `new Circle(..).addGraphic(..)` does not compile. The mistake is caught by
  the compiler rather than by a run-time exception.
- **The bill.** A client that *builds* a tree has to hold a `CompositeGraphic`, so
  uniformity is lost exactly where the structure is assembled.

`composite.bom` and `composite.hw.surveyform` take the transparent side, so the repository
contains both answers and they can be compared rather than asserted.

## What the code does

| Class | Role |
|---|---|
| `Graphic` | **Component** — `draw()`, `paint()`, `shapeCount()`, and child management |
| `Circle` · `Square` · `Triangle` · `Line` | **Leaf** — draw themselves and count as one |
| `CompositeGraphic` · `Canvas` | **Composite** — hold graphics and answer by asking them |

`shapeCount()` is the aggregate worth having: a canvas reports every shape beneath it,
however deep the nesting goes, and `listGraphic()` prints the tree with indentation using the
same recursion.

## Run it with

```bash
mvn -o test -Dtest=GraphicCompositeTest        # 7 tests
java -cp target/classes dev.kaldiroglu.dp.structural.composite.graphic.Main
```
