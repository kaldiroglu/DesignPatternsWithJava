<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `decorator.hw` — worked solutions to the homework

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Three problems set at the end of the Decorator session, solved. **Try them before you read
them** — each is small enough to finish in an evening, and the value is in the arguing.

Each package has a runnable `Main` and a test class.

| Package | Problem | What the code settles |
|---|---|---|
| `invoicepipeline` | Gzip, encrypt, header, checksum — any subset, per customer | 225 bytes against 2033: encrypted bytes do not compress |
| `feeengine` | Platform fee, fixed fee, VAT, discount on one amount | 120.60 against 122.60 — and why a *percentage* discount changes nothing |
| `powerups` | Effects granted mid-fight, which expire | `EffectStack` — what detaching actually costs |

## 1 · `invoicepipeline`

`Pipeline` declares `process` **and** `undo`, so the read chain is the mirror of the write
chain structurally rather than by convention: each stage runs *after* the inner one on the
way out and *before* it on the way back.

Two of the twenty-four orderings are simply wrong, and nothing in the type system says so:

- **Encrypt before compress and the compression achieves nothing.** Measured on a
  1964-byte invoice: compress-then-encrypt is **225 bytes**, encrypt-then-compress is
  **2033** — larger than the input it was given.
- **A checksum belongs outermost**, because it is worth having only over the bytes that
  actually travel. A test proves the digest in the file is the digest of the compressed
  bytes and not of the plain ones.

`Encrypted` is an exclusive-or against a keystream. **It is not encryption** and must never
be used as such; it is here because it has the one property real encryption also has — the
output is high-entropy, so it does not compress.

## 2 · `feeengine`

There is a right answer and it comes from tax law: a discount reduces the consideration the
customer actually pays, so VAT goes **outside** the discount.

The interesting part is `Voucher` against `PromotionalDiscount`. A percentage discount and
a percentage VAT are both multiplications, and multiplication commutes — so with a
percentage the two orderings agree exactly and there is nothing to argue about. Subtracting
a **fixed** amount does not commute, and then the gap between the orderings is precisely the
VAT on the voucher: **2.00** on a ten-lira voucher at 20%.

Both cases are implemented and both are tested. The lesson is not "order always matters", it
is that order is a question you ask of each pair.

## 3 · `powerups`

The stacking half is ordinary Decorator. The removal half is the one with no clean answer.

**You cannot take a link out of a chain.** Every decorator holds the one beneath it and
nothing holds the one above; removing the middle would mean reaching into an object and
replacing what it wraps, which no decorator exposes and none should.

`EffectStack` is the honest answer: it keeps the **recipe** for each effect rather than the
decorator object, and rebuilds the chain whenever the list changes. Granting, revoking and
expiry are list operations; the chain is derived, cheap and thrown away. GoF's Consequence 1
says responsibilities can be "added and removed at run-time simply by attaching and
detaching them" — attaching really is simple, and detaching costs you this class.

`Berserk` forwards **zero** times, which GoF's own description allows. It satisfies the
structure completely and silently discards every effect underneath it: the pattern
constrains the shape of the code, not the honesty of it.

## Run it with

```bash
mvn -o test -Dtest='InvoicePipelineTest,FeeEngineTest,PowerUpsTest'    # 18 tests

java -cp target/classes dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline.Main
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.hw.feeengine.Main
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.hw.powerups.Main
```
