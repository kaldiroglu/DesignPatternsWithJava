<!--
Model: Claude Opus 5.5 (claude-opus-5-5)
Created: 2026-10-29
-->

# `introduction.problems.pricing` — one method, one branch for each campaign

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

A copy of the first stage of the Strategy deck's `behavioral.strategy.pricing.problem`, used
by the Introduction deck, so the introduction package runs on its own. The Strategy deck
uses its own package and goes on to stages two and three and the solution.

| Class | What it does |
|---|---|
| `SwitchingCheckout` | `ring(basket, campaign)`: one `switch` on a string, with a branch for each of five campaigns |
| `SampleBasket` | The basket the run prices |
| `StageOneMain` | Prices the basket under four campaigns, then shows that a typo in a campaign name compiles and fails at run time |
| `domain` | `Basket`, `Customer`, `Line`, `Money`, `Receipt` |

The prices it prints, the five branches in one method, and the run-time failure are
asserted by `SwitchingCheckoutTest`.

## Run it with

```bash
mvn -o -q test -Dtest=SwitchingCheckoutTest
java -cp target/classes dev.kaldiroglu.dp.introduction.problems.pricing.StageOneMain
```
