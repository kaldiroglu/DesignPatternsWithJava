<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `composite.fileSystem` — a file system, with a recursive iterator

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The example everybody already has an intuition for. `size()` answers for a whole subtree in
one call, at any depth, with no loop at the call site.

Child management is on `StorageContainer` rather than `Storage` — the same **safe** choice
`composite.graphic` makes, and the same bill.

## Three defects that were fixed

**`copy()` always returned `null`.** It called `clone()` on a class that never implemented
`Cloneable`, caught the exception, printed a line and returned `null` — on every element,
every time. It is now a real deep copy, detached from any parent, and a test asserts it.

**`move()` did half its job, and a different half depending on the receiver.**
`StorageElement.move` removed the element from its old parent without updating `parent`, so
it believed it still lived where it no longer did. `Directory.move` did the opposite — set
the new parent and joined the target while the old directory went on listing it, so one
directory appeared in two places. Both halves now happen, once, in one place.

**`render()` asked `isDirectory()` and branched** — a type test in the one pattern whose
purpose is to remove them. Each element renders itself; the flag and the method are gone.

Also: `delete()` threw on a root and is now null-safe; `parent` is typed `Directory` rather
than `Storage`, which removed three casts that always succeeded; `Alias` and `ShortCut` were
two identical empty classes and now share a `Link` base that names its target.

## Two decisions worth arguing about

- **A link is its own 64 bytes**, not its target's. Any other answer makes `size()` on a
  root double-count, because the target is counted where it actually lives.
- **`DirectoryIterator` is depth-first over the whole subtree.** It previously returned the
  immediate children, so a nested directory came back as one item and everything under it
  was never seen — which defeats the reason to have an iterator over a Composite at all.

## Run it with

```bash
mvn -o test -Dtest=FileSystemCompositeTest     # 10 tests
java -cp target/classes dev.kaldiroglu.dp.structural.composite.fileSystem.Main
```
