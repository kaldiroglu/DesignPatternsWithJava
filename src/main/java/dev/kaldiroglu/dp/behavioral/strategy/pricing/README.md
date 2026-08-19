# Strategy — checkout campaign pricing

*Claude Opus 5 (claude-opus-5) — Created on 2026-08-19*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

A retailer's till. The campaign changes every Thursday, it is decided by people who do not
write code, and the receipt has to name the campaign that ran and say what it saved.

## The packages

| Package | What is in it |
|---|---|
| `domain` | `Basket`, `Line`, `Customer`, `Money`, `Receipt`. Knows nothing about campaigns. |
| `problem` | Three naive designs, in the order a careful team arrives at them. |
| `solution` | The pattern: `PricingRule`, four rules, `Checkout`, `CampaignBook`. |

## The three naive designs

Each is a real improvement on the one before, and each fails in a new way.

**Stage one — `SwitchingCheckout`.** One method, a branch per campaign, the campaign
arriving as a `String`. It prices correctly. Adding the staff discount edits a method that
already works for four other campaigns, no rule can be tested on its own, and a typo in the
campaign name is a run-time failure.

**Stage two — `EnumCheckout` and `Campaign`.** The codes become an enum and the switch
becomes exhaustive, so the compiler now names the branch you forgot — exactly what it could
not do before. A campaign is still two edits in two files, the till still knows every rule
the company has ever run, and nothing outside the file can add one.

**Stage three — `Checkout` and its subclasses.** One class per campaign. Each rule is
readable on its own page and testable on its own, and a new campaign is a new file. This is
where a careful team lands.

**And then the reversal — `Till`.** The store asks for the obvious thing: give the customer
the best campaign they qualify for. That needs one basket priced several ways, and at stage
three the campaign is the object's *class*. So the caller constructs one till per campaign
and has to name every campaign class in the company to do it. The branch did not go away at
stage three; it moved into the caller and became a list of type names.

## The solution

`PricingRule` has two methods and both are about the algorithm: `name()` and
`priceFor(Basket)`. A rule is handed a basket and answers what it charges. It never asks who
is asking, never decides whether it should be the rule in force, and never touches a
receipt.

`Checkout` holds one and does not know which. There is no `switch` and no `instanceof` in
it — a test strips the comments and checks, because the class documents what it avoids and a
plain search would match its own javadoc.

`CampaignBook` answers GoF's first implementation issue: somebody has to decide which
strategy is in force, and it should be neither the strategies nor the context.

Four classes cover five campaigns, because `PercentageOff` is one class used twice — a rule
with parameters is one class, not one class per parameter.

## Numbers on the slides

Every figure the deck quotes is asserted in
`src/test/java/dev/kaldiroglu/dp/behavioral/strategy/pricing`:

| Claim | Test |
|---|---|
| All three naive designs price identically | `ProblemTest.allThreeAgree` |
| Five campaigns, one method, one file | `ProblemTest.everyCampaignIsABranch` |
| Stage three's caller names three campaign classes | `ProblemTest.theReversal` |
| One till prices one basket five ways | `SolutionTest.oneTillEveryCampaign` |
| A fourth campaign is one class and wins on the day | `SolutionTest.addingACampaignCostsOneClass` |
| The context has no branch over campaigns | `SolutionTest.noBranchInTheContext` |

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest='ProblemTest,SolutionTest'
```
