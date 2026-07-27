<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `decorator.toast` — an Ayvalık toast

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The same pattern as `middleware`, in a domain nobody needs explained. A smaller problem,
and the one most people carry home.

An Ayvalık toast is a toasted sandwich sold by the ingredient: bread costs 5, cheddar
cheese 3, sucuk sausage 3, tomato 2, ketchup 1, Russian salad 2. The customer picks any
combination — and may add another topping **after ordering**, while it is still on the grill.

## The problem — one class per item on the menu

Every toast the shop sells is a class, which means one class per *combination* of toppings.

- **The prices are all correct.** That is why the design survives review.
- **`CheeseSausageToast` is the class that ends the argument.** It wanted to
  `extends CheeseToast, SausageToast`. Java allows one superclass, so the combination is
  **impossible**, not merely tedious, and the prices had to be copied out of both intended
  parents by hand — including a subtraction for the bread that is a guess, because no class
  in the package ever says what the bread costs.
- **The price of tomato is scattered, not stored.** There is no `Tomato` class, so the
  `+ 2` appears in two unrelated subclasses with nothing tying them together.
- **The arithmetic:** five toppings, a class per combination, is 2⁵ − 1 = **31** classes.
  The package has five.

## The solution — one class per topping

`Toastable` (Component), `ToastBread` (ConcreteComponent), `Topping` (Decorator — it *is* a
`Toastable` and *has* a `Toastable`), and five ConcreteDecorators. Five classes replace
thirty-one, and three things become expressible that the problem package cannot state at all:

| | |
|---|---|
| **The same topping twice** | `new Cheese(new Cheese(new ToastBread()))` → 11 |
| **A decorator that multiplies rather than adds** | `Promotion` — and it implements `Toastable` directly rather than extending `Topping`, so the compiler can prove it never appears in `getToppings()` |
| **Order changing the bill** | a 25% discount outermost gives 12; the same discount next to the bread gives 14 |

## What the tests establish — 16 tests

| Test | Count | Point |
|---|---|---|
| `ToastProblemTest` | 5 | The prices are correct; tomato is scattered; cheese-and-sausage forces duplication; the count of 31; toppings are frozen at construction |
| `ToastSolutionTest` | 11 | Transparency; forwarding; the toppings list is not shared; the decorator keeps no collection; the same topping twice; multiply-not-add; order changes the bill; each bread keeps its own name |

Two of those are **regression tests** with a history, and say so in their comments: the
`name` field on `ToastBread` was once `static`, and `Topping` once kept a dead `List` field
that escaped `this` from its constructor. Both were reported by `javac -Xlint`.

## Run it with

```bash
mvn -o test -Dtest='ToastProblemTest,ToastSolutionTest'
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.toast.problem.Main
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.toast.solution.Main
plantuml -tpng uml/*.puml
```
