<!--
Model: Claude Opus 4.8 (1M context)
Created: 2026-06-25
-->

# Adapter Pattern — GoF Drawing-Editor Example

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Project:** GoF Adapter pattern — `Shape` / `TextView` / `TextShape` example
**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* (Gamma, Helm,
Johnson, Vlissides) — Adapter pattern, p. 139; Motivation and Sample Code, p. 146–147.
**Created:** 2026-06-25
**Languages:** Java, C#, Python — one faithful, runnable implementation each.

---

## 1. The story (why this example exists)

GoF motivates Adapter with a **drawing editor**. The editor lets you place lines, polygons,
and text on a canvas, and it manipulates *every* graphic through a single interface called
`Shape`. As long as something is a `Shape`, the editor can ask it for its bounding box, create
a manipulator for it, and so on — without caring what it really is.

Lines and polygons are easy to write as `Shape`s. **Text is the hard one.** But we are lucky:
a rich, well-tested class called `TextView` already exists and does everything we need for
text. The catch — its interface does **not** match `Shape`, and we do **not** want to rewrite
or modify it (it may be third-party, large, or `final`/`sealed`).

> **The mismatch**
> `Shape` speaks: `boundingBox()`, `createManipulator()`, `isEmpty()`
> `TextView` speaks: `getOrigin()`, `getExtent()`, `isEmpty()`

The **Adapter** — `TextShape` — sits between them. It *is* a `Shape` (so the editor accepts it),
and internally it *speaks `TextView`* (so the existing text code is reused, untouched).

---

## 2. The participants (GoF roles → this code)

| GoF role | Here | Responsibility |
|----------|------|----------------|
| **Target** | `Shape` | the interface the client knows how to use |
| **Adaptee** | `TextView` | existing, incompatible class — reused as-is |
| **Adapter** | `TextShape` | converts `TextView` → `Shape` |
| **Client** | `DrawingEditor` | works only through `Shape`, never sees `TextView` |

Supporting cast (so the example is realistic, straight from GoF p. 146):
`Manipulator` / `TextManipulator` / `LineManipulator` (what `createManipulator()` returns),
`LineShape` (a *native* `Shape`, not adapted), and the value objects `Point` and `BoundingBox`.

A full class diagram (both adapter forms, with notes) is in
[`UML/adapter-class-diagram.md`](./UML/adapter-class-diagram.md) — **PlantUML** format, with a
render-ready [`UML/adapter-class-diagram.puml`](./UML/adapter-class-diagram.puml) alongside it.

---

## 3. The two forms of Adapter (both are shown)

GoF describes the pattern in two structural shapes. **This example implements both**, so you can
compare them directly:

### Object adapter — *composition* (the recommended default)
`TextShape` **holds a reference** to a `TextView` and forwards/translates each call.

```
TextShape ──has-a──▶ TextView
```
- One adapter class can wrap **any** `TextView` instance (or subclass).
- File: `TextShape` (e.g. `…/gof/TextShape.java`, `…/Adapter.Gof/TextShape.cs`, `…/gof/text_shape.py`).

### Class adapter — *inheritance*
`classadapter.TextShape` **extends** `TextView` **and implements** `Shape`.

```
classadapter.TextShape ──is-a──▶ TextView
```
- GoF's C++ version uses multiple inheritance (`public Shape, private TextView`). In the
  single-inheritance languages here, the idiom is **extend the adaptee, implement the target**.
- Python uses real multiple inheritance `class TextShape(TextView, Shape)`, where the base
  **order matters**: `TextView` first, so its concrete `is_empty` is found before `Shape`'s
  abstract one.

---

## 4. How the adaptation actually works

The heart of the pattern is `boundingBox()`. The `Shape` client wants a rectangle, but
`TextView` has no such method — it exposes an origin and an extent. The adapter **computes** the
rectangle from those two pieces. *That conversion is the adaptation:*

```
origin   = textView.getOrigin()          // e.g. (10, 10)
extent   = textView.getExtent()          // e.g. (100, 20)  -> width, height
boundingBox = (origin) .. (origin + extent)   // (10,10) -> (110,30)
```

The other two methods illustrate two different bridging tactics:
- `createManipulator()` — a **factory method**; the adapter returns a `TextManipulator`.
- `isEmpty()` — the names happen to match, so the **object** adapter *delegates* to the adaptee,
  while the **class** adapter *inherits* `TextView.isEmpty()` for free.

---

## 5. What the demo prints

Each language ships a `DrawingEditor` that builds a mixed list — a native `LineShape`, two
object-adapted text shapes, and one class-adapted text shape — then loops over them through the
`Shape` interface. **The loop treats adapted and native shapes identically; that uniformity is
the whole point of the pattern.** Sample (Java) output:

```
LineShape (0.0, 0.0)-(40.0, 30.0)
  boundingBox = BoundingBox[(0.0, 0.0) -> (40.0, 30.0)]
  isEmpty     = false
    [LineManipulator] manipulating LineShape (0.0, 0.0)-(40.0, 30.0)
TextShape wrapping TextView["Hello, Adapter"]
  boundingBox = BoundingBox[(10.0, 10.0) -> (110.0, 30.0)]
  isEmpty     = false
    [TextManipulator] manipulating TextShape wrapping TextView["Hello, Adapter"]
...
TextView["Class adapter"]
  boundingBox = BoundingBox[(50.0, 50.0) -> (130.0, 66.0)]
  isEmpty     = false
    [TextManipulator] manipulating TextView["Class adapter"]
```

Both adapter forms produce the **same** bounding box for the same input — proving they are
behaviourally equivalent.

---

## 6. Expected benefits (teaching value)

- Uses the **real GoF example**, so students recognise it when they re-read the book.
- Shows **both** adapter forms side by side in three languages.
- Pays off the deck's `final`/`sealed` problem: the object adapter works even when the adaptee
  cannot be extended.

## 7. Architecture / styles

- Structural design pattern: **Adapter** (GoF), as both class and object adapter.
- One package/namespace per language under the mandated root **`dev.kaldiroglu`**.
- Immutable value objects (`Point`, `BoundingBox`) via records / dataclasses.
- Unit tests per language pin the adaptation behaviour (see each `Test.md`).

