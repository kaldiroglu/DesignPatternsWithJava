# Strategy — freight quoting

*Claude Opus 5 (claude-opus-5) — Created on 2026-08-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Four carriers, four ways of arriving at a price, and one parcel they disagree about.

This example has **no `problem` package**. By the time it appears the audience has seen the
branch, the enum and the class-per-case twice over; repeating them a third time teaches
nothing. What it adds is a family whose members have genuinely nothing in common inside.

## The rate cards

| Card | How it prices | Why it is here |
|---|---|---|
| `ByDesi` | Volume over 3000, billed as kilos, whichever of desi and actual weight is greater | The domestic convention. A pillow costs what a paving slab costs if it fills the same box. |
| `ByWeightBand` | A printed table of bands, one price each | Not a rate at all — a lookup. One gram over a band edge costs a whole band more. |
| `ByZone` | A base price for the route, plus a rate per kilo, plus a fuel surcharge | Holds state the others do not: the surcharge moves with the oil price, not with the contract. |
| `FlatRate` | One price, whatever it is | A strategy may ignore its input entirely and still be a strategy. |

`ByWeightBand` is the one that decides the interface. Three of the four could have been
expressed as `Money perKilo()`, and it could not — the same lesson `pricing` teaches with
buy-two-get-one, in a domain where the awkward case is the normal one.

## The measured comparison

The same two parcels, priced by all four. `FreightTest` asserts every figure:

| | pillow · 0.9 kg, 20 desi | books · 4.2 kg, 1.7 desi |
|---|---|---|
| `Yurtici` (desi) | 760.00 | 190.00 |
| `Aras` (bands) | 190.00 | **70.00** |
| `UPS` (zones) | 345.00 | 138.00 |
| `Marketplace` (flat) | **89.90** | 89.90 |

**The cheapest carrier is not the same carrier.** That is the whole reason the desk holds a
family rather than a contract, and it is why `CarrierBoard` exists: somebody has to choose,
and it should be neither the cards nor the desk.

The flat rate was priced at 89.90 rather than 59.90 for exactly this reason. At 59.90 it won
both parcels, and a comparison whose answer never changes demonstrates nothing.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest='FreightTest'
```
