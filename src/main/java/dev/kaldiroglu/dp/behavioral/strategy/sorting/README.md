# Strategy — the sorter

*Claude Opus 5 (claude-opus-5) — Created on 2026-08-19*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The author's own Strategy example, carried over from `org.javaturk.dp.ch08.strategy.sorting`
in the earlier *Design Patterns 2.0* material. Three sorting algorithms, and which one suits
the array is decided by its size: under a hundred elements bubble, under a million quicksort,
above that hand it to the library.

The choice is genuine engineering rather than indecision, which is what makes the naive
version worth taking seriously before it is taken apart.

## The three stages

| Package | What it is |
|---|---|
| `problem` | One class that both decides and implements. The `if`/`else if` on size sits above a bubble pass, a quicksort and a partition. |
| `subclassing` | Each algorithm a subclass. Readable and testable on its own — and now every caller has to choose, which is the one place that knows nothing about which algorithm suits which input. |
| `pattern` | `Sorter` and three implementations, with `SortingContext` choosing. |

## Why this example is here as well as the checkout

It makes a point the pricing example cannot, and it is the honest one:

**The branch does not disappear.** `SortingContext` tests the same two thresholds
`problem.Sorter` tests. What changed is that the naive class *implemented* three algorithms
and the context *selects* between three objects and implements none. Strategy separates
deciding from doing; it does not delete the deciding.

A deck that promises the `if` goes away is teaching something the code does not do — so this
example is where that gets said, and `SortingTest.theBranchMovedRatherThanVanished` measures
it: both files carry the thresholds, only one carries a `partition`.

The cost is the one GoF list and the author's own slides repeat: as algorithms are added,
that selection method grows. A registry keyed on the input — `pricing.CampaignBook` — is the
usual next step.

## The figures

| Claim | Test |
|---|---|
| All three designs sort identically | `theDesignsAgree` |
| Both designs pick the same algorithm at every size | `theSameThresholds` |
| The decision is testable without sorting a billion doubles | `theDecisionIsSeparable` |
| The branch selects rather than implements | `theBranchMovedRatherThanVanished` |
| A fourth algorithm is one class | `addingAnAlgorithm` |

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest='SortingTest'
```
