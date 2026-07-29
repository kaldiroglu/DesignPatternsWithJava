<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `composite.fileSystem` — a file system, and six questions answered at any depth

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The example everybody already has an intuition for. A directory holds files; it also holds
other directories, which hold files of their own. Ask any element a question and you get an
answer for the whole subtree beneath it — one call, at any depth, with no loop at the call
site.

## The participants

| Class | Role | What it does |
|---|---|---|
| `Storage` | **Component** | The interface every element answers |
| `File` | **Leaf** | Has bytes of its own and no children |
| `Link` · `Alias` · `ShortCut` | **Leaf** | Point at another element and name it |
| `Directory` | **Composite** | Holds `Storage` — files, links or more directories — and answers by asking them |
| `DiskReport` | **Client** | Asks five questions and names one type |
| `StorageElement` | — | Shared state: name, parent, modification time |
| `StorageContainer` | — | Child management, deliberately kept off the Component |

## Six roll-ups, and why these six

| Operation | Kind of aggregation |
|---|---|
| `size()` | a **sum** over the subtree |
| `count()` | a **sum**, and a leaf counts as one |
| `lastModified()` | a **maximum** — aggregating is not only adding |
| `largest()` | a **reduction to an element**, not to a number |
| `find(name)` · `findAll(test)` | a **search** the caller writes no recursion for |
| `render(indent)` | the tree as text, each element rendering itself |

They are chosen on one principle: **these are the operations a real file system does not
branch on either.** `du`, `find` and `ls -lt` treat a file and a directory alike, so the
domain agrees with the pattern instead of arguing with it. Ask a file system to *open* a
thing and it will branch all day — which is why `open` is not on this interface.

`Storage` also carries `rename`, `save`, `delete`, `copy` and `move`. Those act on one
element and aggregate nothing; they are on the Component because every element needs them.
Worth saying plainly: a Component interface grows, and GoF list *"can make your design overly
general"* among this pattern's liabilities. Here you can see exactly which half is which.

## The one idea

Every operation on `Directory` is the same two lines: do this directory's part, then ask the
children to do theirs.

```java
public long size() {
    return DIRECTORY_BYTES + elements.stream().mapToLong(Storage::size).sum();
}
```

`count()`, `lastModified()`, `largest()`, `find()` and `render()` are all that shape. The
recursion lives here, once.

Note what is **not** in `Directory.render()`: it never asks what kind of element it is
holding. Each element renders itself. Taking that question away from callers is the reason to
reach for this pattern at all.

## The client, which is the argument

`DiskReport` answers five questions about a tree of unknown shape and names exactly one type:
`Storage`. No `instanceof`, no `isDirectory()`, no loop. Hand it a single file instead of a
root and every answer is still correct — a test asserts both halves, including that no
concrete element type appears anywhere in its signatures.

## Caching the total — GoF implementation issue 8

`Directory` caches its size and throws the cache away when anything changes:

```java
void invalidate() {
    cachedSize = UNCACHED;
    if (getParent() != null) {
        getParent().invalidate();       // upward, and only upward
    }
}
```

Measured by the tests: three calls to `size()` with nothing changing compute **nothing**, and
adding one file three levels down recomputes **exactly three** totals — its own directory and
the two above it. Nothing below the change is touched.

This is also what makes the `parent` reference load-bearing rather than a convenience for
`path()`. A change invalidates every ancestor's answer and the only way to reach them is up.

## Two decisions worth arguing about

**Child management is on `StorageContainer`, not on `Storage`.** GoF's implementation issue 1,
answered on the **safe** side: `add` does not exist on `File`, so giving a file children is a
compile error rather than a run-time one. The price is that code *building* a tree has to know
it is holding a `Directory`. `composite.graphic` makes the same choice and `composite.bom`
makes the transparent one, so both answers are in the repository.

**A link costs its own 64 bytes, not its target's.** Any other answer makes `size()` on a root
double-count, because the target is already counted where it actually lives. A tree that lies
about its own size is worse than one that cannot be asked.

## The iterator

`DirectoryIterator` walks the whole subtree depth-first rather than returning the immediate
children — the only version worth having over a Composite: a nested directory is not one item,
it is everything underneath it. GoF mention exactly this, and it is where Composite and
Iterator meet.

## What the tests measure

`FileSystemCompositeTest` states every claim above as a number: that each roll-up crosses
three levels, that a leaf answers all six, that the client names one type, and that a single
added file costs exactly three recomputations.

## Run it with

```bash
mvn -o test -Dtest=FileSystemCompositeTest        # 20 tests
java -cp target/classes dev.kaldiroglu.dp.structural.composite.fileSystem.Main
```
