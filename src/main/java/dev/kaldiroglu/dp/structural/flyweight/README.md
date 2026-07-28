# Flyweight

**Claude Opus 5 (1M context) · July 28, 2026**

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

> "Use sharing to support large numbers of fine-grained objects efficiently."
> — *Design Patterns*, p. 195

Flyweight is the one structural pattern whose benefit is a **number**. Every other pattern
in the chapter changes what code can do; this one changes how many objects exist. So every
claim in this package is asserted by a test as a count, never as an adjective.

## The one idea

Split an object's state in two:

| | | |
|---|---|---|
| **Intrinsic** | True of the thing wherever it appears | the letter `e`, the oak's texture, AAPL's ISIN |
| **Extrinsic** | True of *this* appearance only | position on the page, coordinates in the forest, the bid at 09:15:00 |

Store the intrinsic state once and share it. Pass the extrinsic state in as an argument.
The test for whether you have split correctly is blunt: **if two occurrences would fight
over a field, that field is extrinsic.**

## What is in here

| Package | What it is | The number |
|---|---|---|
| `gof` | GoF's document editor — glyphs, rows, columns | 61 characters → **17** objects |
| `book.problem` | A book editor written as Flyweight, that shares nothing | kept as written |
| `book.solution` | The same, corrected | 236 characters → **29** objects |
| `forest.problem` | A tree per tree, texture and all | 10,000 trees → **10,000** texture loads |
| `forest.solution` | A type per kind of tree | 10,000 trees → **2** texture loads |
| `quote.problem` | A market data tick carrying its instrument | 10,000 ticks → **10,000** symbol objects |
| `quote.solution` | Instruments interned in a registry | 10,000 ticks → **1** symbol object |
| `circles.problem` | An animation written as Flyweight, that shares nothing | kept as written |
| `circles.solution` | Style shared, position passed in | 10,000 circles → **≤100** style objects |
| `pool` | **Not Flyweight.** A connection pool, kept as the counter-example | see its README |
| `hw` | Three homework problems, worked | see `hw/README.md` |

## Two examples are kept broken on purpose

`book.problem` and `circles.problem` were both written as illustrations of this pattern and
neither implements it. They are preserved exactly as they were, because what is wrong with
them is the lesson — and each defect is pinned by a test so it cannot be quietly fixed in
the wrong place. Their `package-info.java` files list every one.

The two failures are the same failure, and they interlock:

1. **The factory never pools.** `createCharacter` and `create()` both call `new` every time.
2. **The flyweight stores extrinsic state.** `Character` holds its `position`; `Circle`
   holds its `center`. Both classes even *label* those fields extrinsic in a comment while
   storing them as fields.

The second is why fixing the first alone would introduce a bug rather than the pattern:
share a `Character` that remembers its own position and the second occurrence of a letter
overwrites the first.

## The mistake worth knowing

**An object pool is not a flyweight.** The `pool` package documented itself as one. The
deciding question is whether two holders at the same instant is correct or catastrophic:

- A flyweight is held by **everybody at once**, and that is safe *because* it is immutable.
- A pooled connection is held by **one caller at a time**, is mutable, carries a
  transaction, and must be given back.

Flyweight saves memory. A pool saves time. They look alike only because both hand out
objects that already exist.

## Run it with

```bash
mvn -q compile

# GoF's own example
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.gof.Main

# the corrected book, forest, quotes and circles
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.book.solution.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.forest.solution.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.quote.solution.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.circles.solution.Main

# and the counter-example
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.pool.Main

# every Flyweight test
mvn -q test -Dtest='*Flyweight*,*Glyph*,PoolIsNotFlyweightTest,TileMapTest,LogSourceTest,ParticleSystemTest'

# the whole suite
mvn -q test
```
