<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-08-19
-->

# `bridge.retrofit` — a standard arrives over a system that already works

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The case most people meet before any other, and the one GoF's applicability list does not
state: the system works, its clients work, and a regulator now requires the capability
through *their* interface. Rewriting is out. Changing the interface breaks every caller
inside the company.

## The move

Their interface becomes the Abstraction; the engine you already have becomes an Implementor.

| | |
|---|---|
| `RegulatoryReport` | the required interface, which we do not get to design |
| `QuarterlyReport`, `AuditedReport` | refinements of it — the standard grows its own subtypes |
| `VendorClient` | the engine's own vocabulary: `open`, `pull`, `release` |
| `LegacyEngine`, `PurchasedEngine` | the system from last decade, and the one bought next year |

`submit` is the operation the regulation names, and it exists nowhere in the engine — it is
composed from three of the engine's primitives. `LegacyEngine.reportDirectly` is a caller
from ten years ago that still works, untouched, and does not know the standard exists.

## Why this is a Bridge and not an Adapter

One existing class fitted to one required interface, after the fact, is an **Adapter**, and
should be called one. It is a **Bridge** when both sides are families — the required
interface grows subtypes *and* a second engine appears behind it.

At two by two the counts are equal, four either way, and that is the last moment they are:

| | wrapper per pair | bridge |
|---|---|---|
| 2 reports × 2 engines | 4 | **4** |
| a third report | +2 wrappers | **+1 class** |
| a third engine | +3 wrappers | **+1 class** |
| move a live report to another engine | impossible | `setEngine` |

`RetrofitTest` asserts all of it, and counts the classes from the package rather than doing
arithmetic on literals. It also asserts the thing that keeps this honest: `VendorClient` has
exactly `name`, `open`, `pull`, `release` and **no `submit`** — an implementor that grows to
mirror its abstraction is the failure GoF warn about on p. 154.

## The diagram

`uml/Bridge - Retrofit - Class Diagram.puml`, rendered to PNG and SVG beside it. The
slide-optimized version of the same structure is `Deck/UML/p4-retrofit.puml` in the course
repository — same classes, prose removed, larger type.

## Run it with

```bash
mvn -o test -Dtest=RetrofitTest
```
