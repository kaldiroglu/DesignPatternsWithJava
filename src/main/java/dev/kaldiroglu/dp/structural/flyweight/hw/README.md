# Flyweight — Homework

**Claude Opus 5 (1M context) · July 28, 2026**

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Three problems. Try them before reading these solutions — the point of each one is a
decision you have to make yourself, and reading the answer first removes it.

## 1 · The tile map — `tiles`

A 500×500 world map. Five kinds of ground, each with 64 KB of artwork.

**The decision:** where the sprite gets loaded. If `TileFactory.get` takes a `byte[]`, it
must either key on it or ignore it — and ignoring it is how a winter texture renders as a
summer one with nothing in the log. Loading *inside* the factory removes the argument and
the whole class of bug with it.

**Settled by the tests:** 250,000 squares cost **5** tile objects and **5** sprite loads.
A map 2,500 times larger than another holds exactly the same sprite bytes.

## 2 · The logger — `log`

Millions of log lines from a few hundred call sites.

**The decision:** what goes in the key. Keyed on the class name alone, a line emitted from
`save()` carries `load()`'s identity, and the stack in your log is a lie. The intrinsic
state is the *whole* call site.

**Settled by the tests:** 30,000 lines from 3 call sites retain **3** `LogSource` objects.

## 3 · The particle system — `particles`

A million particles. Four kinds. Position and velocity change sixty times a second.

**The decision:** what to do about the state that *cannot* be shared. It changes constantly,
so it can be neither shared nor made immutable — and the answer is not to abandon the
pattern but to notice that it need not be objects at all. The varying state lives in
parallel `double[]` arrays, so a million particles are not a million objects.

This is the homework that shows the pattern's limit as clearly as its benefit. Flyweight
removes the shared state; keeping the rest out of objects is the natural next step, and GoF
hint at it (p. 199) when they note that extrinsic state is often "computed rather than
stored".

**Settled by the tests:** 1,000,000 particles cost **4** type objects. Gravity reads mass
from the shared type; a heavier particle falls faster with no per-particle copy of anything.

## What to hand in

1. The flyweight, the factory, and a client — with the flyweight holding **no** extrinsic
   state and no setters.
2. A test that **counts**: occurrences against objects. A number, not an adjective.
3. One sentence naming what you decided was intrinsic, and why.
4. The refusal: find something in your own code called a cache or a pool, and say which it
   is. If two holders at once would be a bug, it is not a flyweight.

## Run it with

```bash
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.hw.tiles.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.hw.log.Main
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.hw.particles.Main

mvn -q test -Dtest='TileMapTest,LogSourceTest,ParticleSystemTest'
```
