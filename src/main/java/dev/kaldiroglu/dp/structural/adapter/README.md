<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `adapter` — the Adapter pattern

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Adapter,
pp. 139–150.

> Convert the interface of a class into another interface clients expect. Adapter lets classes
> work together that couldn't otherwise because of incompatible interfaces.

| Package | Example |
|---|---|
| `gof` | The book's own: a drawing editor that manipulates everything as a `Shape`, and a `TextView` that already works but does not fit |
| `electricity` | A Turkish appliance on an American socket — and every implementation variant the chapter discusses |

## The word that matters

**Existing.** An adapter reconciles two interfaces that were designed without each other in
mind, *after the fact*. That is what separates it from its neighbors:

- a **Decorator** keeps the same interface and adds behavior;
- a **Proxy** keeps the same interface and controls access;
- a **Facade** invents a new, simpler interface over many objects;
- an **Adapter** changes an interface because two things that already exist do not fit.

If you can change either side, you do not need this pattern.

## Class or object?

| | Class adapter | Object adapter |
|---|---|---|
| Relationship | **extends** the adaptee | **holds** the adaptee |
| Adaptees | exactly one, fixed at compile time | any, and subclasses of it |
| Can override adaptee behavior | yes | no |
| Usable as the adaptee | yes | no |
| Java restriction | single inheritance spends your one `extends` | none |

Both forms are implemented in both examples, so the difference can be asserted rather than
described — see `classAdapterIsAlsoTheAdaptee` in each test.

## Run it with

```bash
mvn -o test -Dtest='ElectricityAdapterTest,DrawingEditorAdapterTest'    # 16 tests
python3 tools/make_package_diagrams.py
```
