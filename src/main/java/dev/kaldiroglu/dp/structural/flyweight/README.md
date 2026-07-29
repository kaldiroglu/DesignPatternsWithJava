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
| `book.wrong` | The same example as first written — it shares nothing | kept as written |
| `book.correct` | The same example, fixed | 236 characters → **29** objects |
| `forest.problem` | A tree per tree, texture and all | 10,000 trees → **10,000** texture loads |
| `forest.solution` | A type per kind of tree | 10,000 trees → **2** texture loads |
| `quote.problem` | A market data tick carrying its instrument | 10,000 ticks → **10,000** symbol objects |
| `quote.solution` | Instruments interned in a registry | 10,000 ticks → **1** symbol object |
| `pool` | **Not Flyweight.** A connection pool, kept as the counter-example | see its README |
| `hw` | Three homework problems, worked | see `hw/README.md` |

## The book example is in the repository twice

`book.wrong` is the example as it was first written — an illustration of this pattern that
does not implement it. `book.correct` is the same example with the defects fixed. Both are
kept, and both are tested, so the difference can be read side by side rather than described.

The two central failures are worth knowing because they interlock:

1. **The factory never pooled.** `createCharacter` called `new` on every request, while a
   field that was presumably meant to be the pool was never read or written.
2. **The flyweight stored extrinsic state.** `Character` held `line` and `position`, in
   fields the class itself *labelled* "Extrinsic properties" in a comment.

The second is why fixing the first alone would introduce a bug rather than the pattern:
share a `Character` that remembers its own position and the second occurrence of a letter
overwrites the first. `book.correct`'s `Line` holds position as an index instead — it turned
out to need no home at all.

Three smaller defects went with them: `upperCase` was recorded and then ignored, so a
capital T rendered lower case; a line declared itself full at `capacity + 1`; and
`addEndOfLine()` appended without consulting the capacity check at all.

`BookFlyweightTest` tests both packages: four tests pin the defects in `book.wrong` so they
cannot be quietly fixed in the wrong place, and the rest measure what `book.correct` saves.

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

# the corrected book, and the forest and market data examples
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.book.correct.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.forest.solution.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.quote.solution.Main

# and the counter-example
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.pool.Main

# every Flyweight test
mvn -q test -Dtest='*Flyweight*,*Glyph*,PoolIsNotFlyweightTest,TileMapTest,LogSourceTest,ParticleSystemTest'

# the whole suite
mvn -q test
```
